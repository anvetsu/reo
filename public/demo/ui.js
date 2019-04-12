var sampleLabels = ['AE', 'DB', 'MC', 'OC', 'V1'];
var sampleDataSets = [{
    label: 'Order by PaymentTypes',
    data: [12, 19, 3, 5, 2],
    backgroundColor: [
        'rgba(255, 99, 132, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(255, 206, 86, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(153, 102, 255, 0.2)',	
        'rgba(153, 102, 255, 0.2)'
    ],
    borderColor: [
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(75, 192, 192, 1)',	
        'rgba(153, 102, 255, 1)'
    ],
    borderWidth: 1
}];

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
var ptsCode = `grp(orders('paymenttype) inc 0)`

var mottoResult = {"value": "void"}

function evalHandler(result) {
    mottoResult = result;
    s = JSON.stringify(result.value, null, 2);
    if (s == "\"void\"")
	s = "ready";
    var result = ace.edit("result");
    result.setValue(s);
}

function evalMotto(code) {
    console.log(code);
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
    result.setTheme("ace/theme/terminal");
    result.session.setMode("ace/mode/javascript");
    result.setFontSize(18);
    result.renderer.setShowGutter(false);
    result.setValue("");

    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/terminal");
    editor.session.setMode("ace/mode/clojure");
    editor.setFontSize(18);
    editor.setValue(ptsCode);
    editor.commands.addCommand({
	name: 'evalCommand',
	bindKey: {win: 'Ctrl-E',  mac: 'Command-E'},
	exec: function(editor) {
            evalMotto(editor.getValue());
	},
	readOnly: true
    });

    editor.commands.addCommand({
	name: 'chartCommand',
	bindKey: {win: 'Ctrl-K',  mac: 'Command-K'},
	exec: function(editor) {
	    v = mottoResult.value;
	    sampleDataSets.data = Object.values(v);
	    makeBarChart(Object.keys(v), sampleDataSets);
	},
	readOnly: true
    });    

}

$(document).ready(function() {
});
