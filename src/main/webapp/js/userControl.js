$(document).ready(function () {
  getUsers();
  filterUsers();
  processBlockAction(getUsers);
});

function processFiltering() {
  let roleVal = $("#role").val().toLowerCase();
  let fNameVal = $("#firstName").val().toLowerCase();
  let lNameVal = $("#lastName").val().toLowerCase();
  let phoneVal = $("#phone").val().toLowerCase();
  let emailVal = $("#email").val().toLowerCase();
  let cashVal = $("#cash").val().toLowerCase();

  $("#userTable tr").filter(function () {
    $(this).toggle(
      $(this).find("td:eq(0)").text().toLowerCase().indexOf(roleVal) > -1
      && $(this).find("td:eq(1)").text().toLowerCase().indexOf(fNameVal) > -1
      && $(this).find("td:eq(2)").text().toLowerCase().indexOf(lNameVal) > -1
      && $(this).find("td:eq(3)").text().toLowerCase().indexOf(phoneVal) > -1
      && $(this).find("td:eq(4)").text().toLowerCase().indexOf(emailVal) > -1
      && $(this).find("td:eq(5)").text().toLowerCase().indexOf(cashVal) > -1)
  });
  $("#filterCount").text($("#userTable tr:visible").length);
}

function getUsers() {
  $.post("/controller?command=get_users", function (data) {
    data.obj.forEach(myFunction);

    function myFunction(user) {
      let tr = $("<tr></tr>");
      let role = $("<td></td>").text(user.account.role);
      let firstName = $("<td></td>").text(user.firstName);
      let lastName = $("<td></td>").text(user.lastName);
      let phone = $("<td></td>").text(user.account.phone);
      let email = $("<td></td>").text(user.email);
      let cash = $("<td></td>").text(user.cash);
      let button = $('<button type="button"></button>');

      if (user.status === "ACTIVE") {
        button.attr("class", "btn btn-outline-danger btn-block");
        button.text("Block");
      } else {
        button.attr("class", "btn btn-outline-success btn-block");
        button.text("Unblock");
      }

      button.attr("data-id", user.account.id);
      tr.append(role)
        .append(firstName)
        .append(lastName)
        .append(phone)
        .append(email)
        .append(cash)
        .append($("<td></td>").append(button));
      $("#userTable").append(tr);
      processFiltering();
    }
  }, "json");
}

function filterUsers() {
  $("#filter input").on("keyup", function () {
    processFiltering();
  });
}

function createAlert(data) {
  let alertDiv = $('<div style="display: none" class="alert alert-success alert-dismissible fade show">' +
    '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
    '</div>')
  alertDiv.append($("<strong></strong>").text(data.message)).show();
  setTimeout(function () {
    alertDiv.fadeOut();
  }, 10000);
  return alertDiv;
}

//todo
function processBlockAction(getUsers) {
  $(this).on("click", function (e) {
    const id = e.target?.dataset?.id || null;
    if (id) {
      $.post("/controller?command=block_user", {id: id}, function (data) {
        // if(data.obj.)
        // todo add error
        let alertDiv = createAlert(data);

        $("#table").before(alertDiv);
        $("#userTable").empty();
        getUsers();
      }, "json");
    }
  });
}
