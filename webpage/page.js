  var grid;
  var columns = [
    {id: "title", name: "Name", field: "name"},
    {id: "field1", name: "Field1", field: "values", fieldIdx: 0},
    {id: "field2", name: "Field2", field: "values", fieldIdx: 1},
    {id: "field3", name: "Field3", field: "values", fieldIdx: 2}
  ];

  var options = {
    enableCellNavigation: true,
    enableColumnReorder: false,
    dataItemColumnValueExtractor: getItemColumnValue
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
    for (var i = 0; i < 500; i++) {
      data[i] = {
        name: "Item " + i,
        values: [
          Math.round(Math.random() * 100),
          Math.round(Math.random() * 100),
          Math.round(Math.random() * 100)
        ]
      };
    }

    grid = new Slick.Grid("#myGrid", data, columns, options);
  })