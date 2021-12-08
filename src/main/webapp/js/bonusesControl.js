$(document).ready(function () {
  const bonusTable = createTable([]);

  fillBonusesTable(bonusTable);
  addFilterInput();
  addFilterListener(bonusTable);
  addModalButtonsListener(bonusTable);
  addNewBonusButtonListener(bonusTable);
});

function clearErrorFields() {
  $("#discountFeedback").text("");
  $("#usersFeedback").text("");
  $("#expireDateFeedback").text("");
}

function showInvalidFields(data) {
  clearErrorFields();
  if (data.discount) {
    showErrorMessage($("#discountFeedback"), data.discount);
  }
  if (data.users) {
    showErrorMessage($("#usersFeedback"), data.users);
  }
  if (data.expireDate) {
    showErrorMessage($("#expireDateFeedback"), data.expireDate);
  }
}

function showErrorMessage($field, errorMsg) {
  $field.text(errorMsg);
  $field.show("slow");
}

function addFilterInput() {
  $("#filterInputs").append(
    `<li class="list-group-item d-flex justify-content-between lh-condensed">
        <div>
          <h6 class="my-0">Order amount</h6>
          <input class="form-control" id="orderAmount" type="text">
        </div>
      </li>`
  );
}

function processFiltering(table) {
  const fNameVal = $("#firstName").val().toLowerCase();
  const lNameVal = $("#lastName").val().toLowerCase();
  const phoneVal = $("#phone").val().toLowerCase();
  const discount = $("#discount").val().toLowerCase();
  const expireDate = $("#expireDate").val().toLowerCase();

  table.clearFilter();
  table.setFilter([
    {field: "client.firstName", type: "like", value: fNameVal},
    {field: "client.lastName", type: "like", value: lNameVal},
    {field: "client.account.phone", type: "like", value: phoneVal},
    {field: "discount", type: "starts", value: discount},
    {field: "expires", type: "like", value: expireDate},
  ]);
  $("#filterCount").text(table.getDataCount("active"));
}

function fillBonusesTable(table) {
  $.post("/controller?command=get_bonuses", function (data) {
    table.replaceData(data.obj);
    processFiltering(table);
  }, "json");
}

function createAlert(data) {
  const alertDiv = $('<div><button type="button" class="close"' +
    ' data-dismiss="alert">&times;</button></div>')
  console.log(data);
  if (data.status === "SUCCESS") {
    alertDiv.attr("class", "alert alert-success alert-dismissible fade show");
  } else {
    alertDiv.attr("class", "alert alert-danger alert-dismissible fade show");
  }
  alertDiv.append($("<strong></strong>").text(data.message));
  setTimeout(function () {
    alertDiv.fadeOut();
  }, 10000);

  return alertDiv;
}

function addFilterListener(table) {
  $("#filter input").on("keyup", function () {
    processFiltering(table);
  });
}

function showModal(e, cell) {
  const id = e.target?.dataset?.id || null;

  if (id) {
    const $modalHeader = $("#modalHeader");
    const $modalMessage = $("#modalMessage");

    $($modalHeader).find('h5:first').remove();
    $modalMessage.text("");
    $("#approveButton").attr("data-id", id);
    $modalHeader.prepend('<h5 class="modal-title">Approve deletion</h5>');
    $modalMessage.append('<p>Are you really want to delete users bonus?</p>');
    $("#modalApprove").modal();
  }
}

function addModalButtonsListener(table) {
  $("#approveButton").on("click", function (e) {
    const id = e.target?.dataset?.id || null;
    if (id) {
      $.post("/controller?command=delete_bonus", {id: id})
        .done(function (data) {
          const alertDiv = createAlert(data);

          $("#table").before(alertDiv);
          fillBonusesTable(table);
          $("#modalApprove").modal("hide");
        }, "json");
    }
  });
}

const printButton = function (cell, formatterParams, onRendered) {
  const bonus = cell.getRow().getData();

  return '<button type="button" data-id="' + bonus.id +
    '" class="btn btn-outline-danger btn-block">Delete</button>'
};

function createTable(bonuses) {
  return new Tabulator("#table", {
    data: bonuses,
    layout: "fitColumns",
    responsiveLayout: "hide",
    addRowPos: "top",
    history: false,
    pagination: true,
    paginationButtonCount: "5",
    paginationSize: 11,
    movableColumns: false,
    resizableRows: true,
    placeholder: "No Data Available",
    initialSort: [
      {column: "discount", dir: "asc"},
    ],
    columns: [
      {title: "First name", field: "client.firstName", hozAlign: "center"},
      {title: "Last name", field: "client.lastName", hozAlign: "center"},
      {title: "Phone", field: "client.account.phone", hozAlign: "center", width: 170},
      {title: "Discount, %", field: "discount", hozAlign: "center"},
      {title: "Expire date", field: "expires", hozAlign: "center", width: 200},
      {
        title: "Control",
        hozAlign: "center",
        width: 100,
        formatter: printButton,
        cellClick: function (e, cell) {
          showModal(e, cell);
        }
      },
    ],
  });
}

function createUserTable(usersByOrderAmount) {
  return new Tabulator("#users", {
    data: usersByOrderAmount,
    layout: "fitColumns",
    responsiveLayout: "hide",
    addRowPos: "top",
    history: false,
    pagination: true,
    paginationButtonCount: "3",
    paginationSize: 5,
    movableColumns: false,
    resizableRows: true,
    selectable: true,
    placeholder: "No Data Available",
    initialSort: [
      {column: "orderAmount", dir: "asc"},
    ],
    columns: [
      {title: "First name", field: "firstName", hozAlign: "center"},
      {title: "Last name", field: "lastName", hozAlign: "center"},
      {title: "Order amount", field: "orderAmount", hozAlign: "center", width: 170},
    ],
  });
}

function createUsersWithExtraField(data) {
  const myMap = new Map(Object.entries(data.obj));
  let users = [];

  function logMapElements(value, key, map) {
    let user = JSON.parse(key);
    user.orderAmount = value;
    users.push(user);
  }

  myMap.forEach(logMapElements);

  return users;
}

function addNewBonusButtonListener(bonusTable) {
  $('#newBonusButton').on('click', function () {
    $.post("/controller?command=get_users_by_order_amount", function (data) {
      $("#bonusForm").find("#users").children().remove();
      const users = createUsersWithExtraField(data);
      let userTable = createUserTable(users);

      addSelectionButtonListeners(userTable);
      giveBonusButtonListener(bonusTable, userTable);
      clearErrorFields();
    }, "json");
  })
}

function addSelectionButtonListeners(userTable) {
  $("#allUsers").click(function () {
    userTable.selectRow();
  })
  $("#noneUsers").click(function () {
    userTable.deselectRow();
  })
}

function giveBonusButtonListener(bonusTable, userTable) {
  $("#bonusForm").submit(function (event) {
    event.preventDefault();
    let idArray = [];

    for (const selectedUsers of userTable.getSelectedData()) {
      idArray.push(selectedUsers.id);
    }

    const dataToBeSent = $(this).serialize() + "&idArray=" + idArray.join(',');
    $.post($(this).attr("action"), dataToBeSent,
      function (data) {
        if (data.status === "ERROR") {
          showInvalidFields(data.obj);
        } else {
          const alertDiv = createAlert(data);

          $("#table").before(alertDiv);
          $("#bonusModal").modal("hide");
          fillBonusesTable(bonusTable);
        }
      }, "json")
  });
}
