var initTablesCode =
`types:['i 'i 'i "yyyy-MM-dd 00:00:00" 's 's 's 's 'f 'f 'i]
csvopts:dict('delim \\tab 'types types)
orders:csv("db/orders.txt" csvopts)
types:['i 'i 'i "yyyy-MM-dd 00:00:00" "yyyy-MM-dd 00:00:00" 'f 'i 'f]
csvopts:assoc(csvopts 'types types)
orderlines:csv("db/orderlines.txt" csvopts)
['tables ['orders, 'orderlines]]`

var inited = false;

var mottoResult = {"value": "void"}

var mottofns = ["parse", "big", "sml",
		"dt", "sdt", "now",
		"dt_add", "dt_get", "cf",
		"take", "drop", "conj", "fold",
		"map", "filter", "sum", "dif",
		"prd", "qt", "mx", "mn", "max", "min",
		"sums", "difs", "prds", "qts", "mxs", "mns",
		"til", "twins", "collect", "count", "counts",
		"count_group", "count_f", "count_eq", "tab",
		"cols", "top", "group", "data_source",
		"open", "close", "stmt", "qry", "cmd",
		"csv", "csv_fmt", "csv_ahdr", "csv_hdr",
		"csv_delim", "csv_rd"];

function randInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

function randColor() {
    var x = randInt(256);
    if (x < 50) {
	x = x+100;
    }
    return x;
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

function resetChartCanvas() {
    $('#chart').remove();
    $('#chartPanel').append('<canvas id="chart"><canvas>');
}

function makeBarChart(data, stacked) {
    var labels = data[0];
    var datasets = data[1];
    resetChartCanvas();
    var ctx = document.getElementById('chart').getContext('2d');
    var barChart = new Chart(ctx, {
	type: 'bar',
	backgroundColor: "#757575",
	data: {
            labels: labels,
            datasets: datasets
	},
	options: {
            scales: {
		xAxes: [{
		    stacked: stacked
		}],
		yAxes: [{
		    stacked: stacked,
                    ticks: {
			beginAtZero: true
                    }
		}]
            }
	}
    });
}

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
    if (inited == false) {
	inited = true;
	var editor = ace.edit("editor");
	editor.setReadOnly(false);
	editor.focus();
    }
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

function makeDs(data, label) {
    var ds = {};
    ds.label = label;
    ds.borderWidth = 1;
    ds.data = data;
    var [bg, brdr] = randomColors(data.length, 0.2, 1);
    ds.backgroundColor = bg;
    ds.borderColor = brdr;
    return ds;
}

function makeDataSet(r) {
    var v = r[1];
    var data = Object.values(v);
    var ds = makeDs(data, r[0]);
    return [Object.keys(v), [ds]];
}

function makeStakedDataSet(rs) {
    var dss = [];
    var v = null;
    for (i in rs) {
	var r = rs[i];
	v = r[1];
	var data = Object.values(v);
	var ds = makeDs(data, r[0]);
	dss.push(ds);
    }
    return [Object.keys(v), dss];
}

function mkautocompletes() {
    var autocs = [];
    for (i in mottofns) {
	autocs.push({value: mottofns[i], score: 1000, meta: "motto-lib"});
    }
    return autocs;
}

function initUi() {
    var result = ace.edit("result");
    result.setTheme("ace/theme/iplastic");
    result.session.setMode("ace/mode/javascript");
    result.setFontSize(18);
    result.renderer.setShowGutter(false);
    result.setAutoScrollEditorIntoView(true);
    result.setReadOnly(true);
    result.setValue(`loading datasets, please wait...`);

    ace.require("ace/ext/language_tools");
    var editor = ace.edit("editor");
    editor.setOptions({
	enableBasicAutocompletion: true
    });
    editor.completers.push({
	getCompletions: function(editor, session, pos, prefix, callback) {
	    callback(null, mkautocompletes())
	}
    });
    editor.setTheme("ace/theme/textmate");
    editor.session.setMode("ace/mode/rust");
    editor.setFontSize(18);
    editor.setReadOnly(true);
    editor.setValue('');

    evalMotto(initTablesCode);

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
	    var ds = null;
	    var stacked = false;
	    if (r[0] instanceof Object) {
		ds = makeStakedDataSet(r);
		stacked = true;
	    }else
		ds = makeDataSet(r);
	    makeBarChart(ds, stacked);
	},
	readOnly: true
    });    

}

$(document).ready(function() {
    //$('#result_table').DataTable();
});
