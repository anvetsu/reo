function randInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

function randColor() {
    return randInt(256);
}

function randomColors(n, alpha1, alpha2) {
    var bg = [];
    var brdr = [];
    for (var i=0; i<n; i++) {
	var [r, g, b] = [randColor(), randColor(), randColor()]
	bg.push('rgba(' + r + ',' + g + ',' + b + ',' + alpha1 + ')')
	brdr.push('rgba(' + r + ',' + g + ',' + b + ',' + alpha2 + ')')
    }
    return [bg, brdr];
}

function makeBarChart(labels, datasets) {
    var ctx = document.getElementById('chart').getContext('2d');
    var barChart = new Chart(ctx, {
	type: 'bar',
	data: {
            labels: labels,
            datasets: datasets
	},
	options: {
            scales: {
		yAxes: [{
                    ticks: {
			beginAtZero: true
                    }
		}]
            }
	}
    });
}

var initTablesCode =
    `types:['i 'i 'i "yyyy-MM-dd 00:00:00" 's 's 's 's 'f 'f 'i]
     orders:csv("db/orders.txt" dict('delim \\tab 'types types))`
var ptsCode = ''

var mottoResult = {"value": "void"}

function evalHandler(result) {
    mottoResult = result;
    if (typeof result === 'object' && typeof result.value["-meta-"] === 'object') {
	var value = result.value;
	delete value["-meta-"];
    }
    s = JSON.stringify(result.value, null, 2);
    if (s == "\"void\"")
	s = "ready";
    var r = ace.edit("result");
    r.setValue(s, -1);
}

function evalMotto(code) {
    $.ajax({
	type: 'POST',
	url: '/eval',
	data: JSON.stringify({"expr": code}),
	success: evalHandler,
	contentType: "application/json",
	dataType: 'json'
    });
}

function initUi() {
    evalMotto(initTablesCode);

    var result = ace.edit("result");
    result.setTheme("ace/theme/iplastic");
    result.session.setMode("ace/mode/javascript");
    result.setFontSize(18);
    result.renderer.setShowGutter(false);
    result.setAutoScrollEditorIntoView(true);
    result.setReadOnly(true);
    result.setValue("");

    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/textmate");
    editor.session.setMode("ace/mode/rust");
    editor.setFontSize(18);
    editor.setValue(ptsCode);

    editor.commands.addCommand({
	name: 'evalScript',
	bindKey: {win: "Ctrl-.",  mac: "Command-."},
	exec: function(editor) {
            evalMotto(editor.getValue());
	},
	readOnly: true
    });

    editor.commands.addCommand({
	name: 'evalLine',
	bindKey: {win: 'Ctrl-;',  mac: 'Command-;'},
	exec: function(editor) {
	    var pos = editor.getCursorPosition();
	    var row = pos.row;
            evalMotto(editor.session.getLine(row));
	},
	readOnly: true
    });

    editor.commands.addCommand({
	name: 'chartCommand',
	bindKey: {win: 'Ctrl-K',  mac: 'Command-K'},
	exec: function(editor) {
	    var r = mottoResult.value;
	    var label = r[0];
	    var v = r[1];
	    var data = Object.values(v);
	    var ds = {}
	    ds.label = label;
	    ds.borderWidth = 1;
	    ds.data = data;
	    var [bg, brdr] = randomColors(data.length, 0.2, 1);
	    ds.backgroundColor = bg;
	    ds.borderColor = brdr;
	    makeBarChart(Object.keys(v), [ds]);
	},
	readOnly: true
    });    

}

$(document).ready(function() {
    //$('#result_table').DataTable();
});
