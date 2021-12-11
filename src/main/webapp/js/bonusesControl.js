$(document).ready(function () {
  const bonusTable = createTable([]);

  fillBonusesTable(bonusTable);
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
    console.log(data.obj);
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

function showModal(e, headerMsg, bodyMessage) {
  const id = e.target?.dataset?.id || null;

  if (id) {
    const $modalHeader = $("#modalHeader");
    const $modalMessage = $("#modalMessage");

    $($modalHeader).find('h5:first').remove();
    $modalMessage.text("");
    $("#approveButton").attr("data-id", id);
    $modalHeader.prepend('<h5 class="modal-title">' + headerMsg + '</h5>');
    $modalMessage.append('<p>' + bodyMessage + '</p>');
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

const printButton = function (cell) {
  const bonus = cell.getRow().getData();

  return '<button type="button" data-id="' + bonus.id +
    '" class="btn btn-outline-danger btn-block"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">' +
    '  <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>' +
    '  <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>' +
    '</svg></button>'
};

function createUsersWithExtraField(data) {
  const myMap = new Map(Object.entries(data.obj));
  let users = [];

  function logMapElements(value, key) {
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
