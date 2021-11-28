$(document).ready(function () {
  getApplications();
  filterApplications();
  // processBlockAction(getApplications);
});

function processFiltering() {
  let value = $("#searchInput").val().toLowerCase();
  $("#cardContainer>div").filter(function () {
    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
  });

  $("#filterCount").text($("#cardContainer>div:visible").length);
}

function createCard(driver) {
  let card = $(`
      <div class="col-md-4">
        <div id="cardHeader" class="card border-secondary mb-4 box-shadow"></div>
      </div>`);
  let cardTitle = createCardTitle(driver);
  let image = $('<img class="card-img-top" width="100px" ' +
    'src="../images/taxes/' + driver.taxi.photoFilepath + '" alt="Card image cap">');
  let cardBody = createCardBody(cardTitle, driver);

  card.children()
    .append(image)
    .append(cardBody);

  return card
}

function createCardTitle(driver) {
  let cardTitle = $('<h5 class="card-title">Volkswagen Polo</h5>');
  let status = $('<span>' + driver.driverStatus + '</span>');

  if (driver.driverStatus === "PENDING") {
    status.attr("class", "badge badge-pill badge-warning");
  } else if (driver.driverStatus === "REJECTED") {
    status.attr("class", "badge badge-pill badge-danger");
  } else {
    status.attr("class", "badge badge-pill badge-success");
  }//todo add another statuses
  cardTitle.append(status);
  return cardTitle;
}

function createCardDetails(driver) {
  let cardDriver = $('<span><strong>Driver: </strong></span>')
    .append("<span>" + driver.user.lastName + " " + driver.user.firstName + "</span><br>");
  let cardPhone = $('<span><strong>Phone: </strong></span>')
    .append("<span>" + driver.user.account.phone + "</span><br>");
  let cardEmail = $('<span><strong>Email: </strong></span><span>' + driver.user.email + '</span><br><br>');
  let cardButton = $(`
              <div class="d-flex justify-content-between align-items-center">
                <div class="btn-group">
                  <button type="button" data-id="` + driver.user.account.id + `" 
                  class="btn btn-sm btn-outline-info">View details</button>
                </div>
              </div>`);

  return {cardDriver, cardPhone, cardEmail, cardButton};
}

function createCardBody(cardTitle, driver) {
  let cardBody = $('<div class="card-body"></div>');
  let {cardDriver, cardPhone, cardEmail, cardButton} = createCardDetails(driver);

  cardBody.append(cardTitle)
    .append(cardDriver)
    .append(cardPhone)
    .append(cardEmail)
    .append(cardButton);

  return cardBody;
}

function getApplications() {
  $.post("/controller?command=get_driver_applications", function (data) {
    data.obj.forEach(myFunction);

    function myFunction(driver) {
      let card = createCard(driver);

      $("#cardContainer").append(card);
      processFiltering();
    }
  }, "json");
}

function filterApplications() {
  $("#searchInput").on("keyup", function () {
    processFiltering();
  });
}

// function createAlert(data) {
//   let alertDiv = $('<div style="display: none" class="alert alert-success alert-dismissible fade show">' +
//     '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
//     +'</div>')
//   alertDiv.append($("<strong></strong>").text(data.message)).show();
//   setTimeout(function () {
//     alertDiv.fadeOut();
//   }, 10000);
//   return alertDiv;
// }

// function processBlockAction(getUsers) {
//   $(this).on("click", function (e) {
//     const id = e.target?.dataset?.id || null;
//     if (id) {
//       $.post("/controller?command=block_user", {id: id}, function (data) {
//         // if(data.obj.)
//         // todo add error
//         let alertDiv = createAlert(data);
//
//         $("#table").before(alertDiv);
//         $("#userTable").empty();
//         getUsers();
//       }, "json");
//     }
//   });
// }
