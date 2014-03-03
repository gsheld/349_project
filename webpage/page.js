var jsonObject =   {
	'columnNumber': 4,
	'columnData': {
			'Title1': ['datapoint1', 'datapoint2'],
			'Title2': ['datapoint1', 'datapoint2'],
			'Title3': ['datapoint1', 'datapoint2'],
			'Title4': ['datapoint1', 'datapoint2']
	},
	'query': 'some random query',
	'tableTitle': 'Table Title'
};

var objectKeyList = [];
for (property in jsonObject.columnData) {
	objectKeyList.push(property);
}

var grid;
var columns = [];

for (var col = 0; col < jsonObject.columnNumber; col++) {
	columns.push({id: objectKeyList[col], name: objectKeyList[col], field: 'values', fieldIdx: col});
}

var options = {
	enableCellNavigation: true,
	enableColumnReorder: true,
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
	for (var row = 0; row < 2; row++) {
		var values = []
		for (var col = 0; col < jsonObject.columnNumber; col++) {
			values.push(jsonObject.columnData[objectKeyList[col]][row]);
		}
		data[row] = {
			name: 'Item ' + row,
			values: values
		};
	}

	document.getElementById('queryStatement').innerHTML += ['<p>', jsonObject.query, '</p>'].join('')
	grid = new Slick.Grid('#myGrid', data, columns, options);
	grid = new Slick.Grid('#myGrid2', data, columns, options);
});