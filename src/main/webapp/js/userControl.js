$(document).ready(function () {
  let table = createTable([]);

  getUsers(table);

  addFilterListener(table);
  addBlockButtonListener(table);
});

function processFiltering(table) {
  let roleVal = $("#role").val().toLowerCase();
  let fNameVal = $("#firstName").val().toLowerCase();
  let lNameVal = $("#lastName").val().toLowerCase();
  let phoneVal = $("#phone").val().toLowerCase();
  let emailVal = $("#email").val().toLowerCase();
  let cashVal = $("#cash").val().toLowerCase();

  table.clearFilter();
  table.setFilter([
    {field: "account.role", type: "like", value: roleVal},
    {field: "firstName", type: "like", value: fNameVal},
    {field: "lastName", type: "like", value: lNameVal},
    {field: "account.phone", type: "like", value: phoneVal},
    {field: "email", type: "like", value: emailVal},
    {field: "cash", type: "like", value: cashVal},
  ]);
  $("#filterCount").text(table.getDataCount("active"));
}

function getUsers(table) {
  $.post("/controller?command=get_users", function (data) {
    table.replaceData(data.obj);
    processFiltering(table);
  }, "json");
}

function addFilterListener(table) {
  $("#filter input").on("keyup", function () {
    processFiltering(table);
  });
}

function createAlert(data) {
  let alertDiv = $('<div class="alert alert-success alert-dismissible fade show">' +
    '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
    +'</div>')
  alertDiv.append($("<strong></strong>").text(data.message));
  setTimeout(function () {
    alertDiv.fadeOut();
  }, 10000);
  return alertDiv;
}

function addBlockButtonListener(table) {
  $(this).on("click", function (e) {
    const id = e.target?.dataset?.id || null;
    if (id) {
      $.post("/controller?command=block_user", {id: id}, function (data) {
        let alertDiv = createAlert(data);

        $("#table").before(alertDiv);
        getUsers(table);
      }, "json");
    }
  });
}

let printButton = function (cell, formatterParams, onRendered) { //plain text value
  let user = cell.getRow().getData();

  if (user.status === "ACTIVE") {
    return '<button type="button" data-id="' + user.account.id + '" class="btn btn-outline-success btn-block">Active</button>'
  } else {
    return '<button type="button" data-id="' + user.account.id + '" class="btn btn-outline-danger btn-block">Blocked</button>'
  }
};

function createTable(users) {
  return new Tabulator("#table", {
    data: users,
    layout: "fitColumns",
    responsiveLayout: "hide",
    addRowPos: "top",
    history: false,
    pagination: true,
    paginationButtonCount: "5",
    paginationSize: 11,
    movableColumns: false,
    resizableRows: true,
    initialSort: [             //set the initial sort order of the data
      {column: "account.role", dir: "asc"},
    ],
    columns: [
      {title: "Role", field: "account.role", editor: "input", hozAlign: "center"},
      {title: "First name", field: "firstName", hozAlign: "center"},
      {title: "Last name", field: "lastName", hozAlign: "center"},
      {title: "Phone", field: "account.phone", hozAlign: "center", width: 170},
      {title: "Email", field: "email", hozAlign: "center", width: 200},
      {title: "Cash", field: "cash", sorter: "date", hozAlign: "center", width: 80},
      {
        title: "Status",
        field: "status",
        hozAlign: "center",
        width: 100,
        formatter: printButton,
      },
    ],
  });
}
