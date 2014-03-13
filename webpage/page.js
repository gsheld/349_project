$.ajax({
	type: 'GET',
	url: 'http://murphy.wot.eecs.northwestern.edu/~gms130/349_project/webpage/returnTable.py',
	data: {},
	datatype:'script',
	async: false,

	success: function(response) {
		// response is string, convert it to json and apply conditions.
		var json_obj= eval('(' + response + ')');//$.parseJSON('''+response+''');
		if (json_obj.type == 'Error'){
  			alert(json_obj.msg);
		}
		else {
  			alert(json_obj.msg);
		}// else closed
	}, // success closed

	error:function(xhr,err)
	{
    	alert('Error connecting to server, please contact system administator.');
	}
})//ajax closed

var jsonObject =   {
	'columnNumber': 4,
	'columnData': {
			'Title1': ['datapoint1', 'datapoint2', 'datapoint3', 'datapoint4', 'datapoint5', 'datapoint6', 'datapoint7', 'datapoint8', 'datapoint9'],
			'Title2': ['datapoint1', 'datapoint2', 'datapoint3', 'datapoint4', 'datapoint5', 'datapoint6', 'datapoint7', 'datapoint8', 'datapoint9'],
			'Title3': ['datapoint1', 'datapoint2', 'datapoint3', 'datapoint4', 'datapoint5', 'datapoint6', 'datapoint7', 'datapoint8', 'datapoint9'],
			'Title4': ['datapoint1', 'datapoint2', 'datapoint3', 'datapoint4', 'datapoint5', 'datapoint6', 'datapoint7', 'datapoint8', 'datapoint9']
	},
	'query': 'some random query',
	'tableTitle': 'Table Title'
};

/* Gets column titles. */
var objectKeyList = [];
for (property in jsonObject.columnData) {
	objectKeyList.push(property);
}

var grid;
var columns = [];

/* Setup columns. */
for (var col = 0; col < jsonObject.columnNumber; col++) {
	columns.push({id: objectKeyList[col], name: objectKeyList[col], field: 'values', fieldIdx: col});
}

/* Table options. */
var options = {
	enableCellNavigation: false,
	enableColumnReorder: false,
	dataItemColumnValueExtractor: getItemColumnValue,
	autoHeight: true,
	forceFitColumns: true
};

// Get the item column value using a custom 'fieldIdx' column param
function getItemColumnValue(item, column) {
	var values = item[column.field];
	if (column.fieldIdx !== undefined) {
		return values && values[column.fieldIdx];
	} else {
		return values;
	}
}

$(function () {
	var data = [];
	for (var row = 0; row < jsonObject.columnData[objectKeyList[0]].length; row++) {
		var values = []
		for (var col = 0; col < jsonObject.columnNumber; col++) {
			values.push(jsonObject.columnData[objectKeyList[col]][row]);
		}
		data[row] = {
			name: 'Item ' + row,
			values: values
		};
	}

	document.getElementById('queryStatement').innerHTML += ['\n"', jsonObject.query, '"'].join('')
	grid = new Slick.Grid('#myGrid', data, columns, options);
});