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
    $('#result').html(JSON.stringify(result.value));
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
    $('#code').value = ptsCode;
}

$(document).ready(function() {
    $('#btnEval').click(function() {
	evalMotto($('#code')[0].value);
    });
    $('#btnChart').click(function() {
	v = mottoResult.value;
	sampleDataSets.data = Object.values(v);
	makeBarChart(Object.keys(v), sampleDataSets);
    });    
});
