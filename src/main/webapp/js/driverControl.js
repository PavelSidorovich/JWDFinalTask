$(document).ready(function () {
  getApplications();
  addFilterListener();
  addViewButtonListener();
});

function addListenerToRejectButton(getApplications) {
  $("#rejectButton").on("click", function (e) {
    const id = e.target?.dataset?.id || null;
    if (id) {
      $.post("/controller?command=update_driver_status", {id: id, driverStatus: "REJECTED"})
        .done(function (data) {
          if (data.obj.status === "ERROR") {
            $("#cardContainer").before(createAlert("ERROR",
              "Fail on rejection driver application"));
          } else {
            $("#cardContainer").before(createAlert("SUCCESS",
              "Successful rejecting driver application"));
          }
          getApplications();
        }, "json");
    }
    $("#driverApplicationModal").modal("hide");
  })
}

function addListenerToApproveButton(getApplications) {
  $("#approveButton").on("click", function (e) {
    const id = e.target?.dataset?.id || null;
    if (id) {
      $.post("/controller?command=update_driver_status", {id: id, driverStatus: "BUSY"})
        .done(function (data) {
          if (data.obj.status === "ERROR") {
            $("#cardContainer").before(createAlert("ERROR",
              "Fail on approving driver application"));
          } else {
            $("#cardContainer").before(createAlert("SUCCESS",
              "Successful approving driver application!"));
          }
          getApplications();
        }, "json");
    }
    $("#driverApplicationModal").modal("hide");
  })
}

function addModalWindowButtonListeners() {
  addListenerToRejectButton(getApplications);
  addListenerToApproveButton(getApplications);
}

function createAlert(status, message) {
  let alertDiv = $('<div><button type="button" class="close"' +
    ' data-dismiss="alert">&times;</button></div>')

  status === "ERROR"
    ? alertDiv.attr("class", "alert alert-danger alert-dismissible fade show")
    : alertDiv.attr("class", "alert alert-success alert-dismissible fade show");
  alertDiv.append($("<strong></strong>").text(message));
  setTimeout(function () {
    alertDiv.fadeOut();
  }, 10000);

  return alertDiv;
}

function processFiltering() {
  const value = $("#searchInput").val().toLowerCase();

  $("#cardContainer>div").filter(function () {
    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
  });
  $("#filterCount").text($("#cardContainer>div:visible").length);
}

function getCarImage(driver) {
  return $('<img class="card-img-top" width="100px" ' +
    'src="../images/taxes/' + driver.taxi.photoFilepath + '" alt="Card image cap">');
}

function createCard(driver) {
  const card = $(`
      <div class="col-md-4">
        <div id="cardHeader" class="card border-secondary mb-4 box-shadow"></div>
      </div>`);
  const cardTitle = createCardTitle(driver);
  const image = getCarImage(driver);
  const cardBody = createCardBody(cardTitle, driver);

  card.children()
    .append(image)
    .append(cardBody);

  return card;
}

function getCardCar(driver) {
  return $('<h5 class="card-title">' + driver.taxi.carBrand
    + " " + driver.taxi.carModel + '</h5>');
}

function getStatus(driver) {
  const cardStatus = $('<span>' + driver.driverStatus + '</span>');

  if (driver.driverStatus === "PENDING") {
    cardStatus.attr("class", "badge badge-pill badge-warning");
  } else if (driver.driverStatus === "REJECTED") {
    cardStatus.attr("class", "badge badge-pill badge-danger");
  } else {
    cardStatus.attr("class", "badge badge-pill badge-success");
  }

  return cardStatus;
}

function createCardTitle(driver) {
  const cardTitle = getCardCar(driver);
  const status = getStatus(driver);

  cardTitle.append(status);

  return cardTitle;
}

function getCardButton(driver) {
  return $(`
              <div class="d-flex justify-content-between align-items-center">
                <div class="btn-group">
                  <button type="button" data-id="` + driver.user.account.id + `" 
                  class="btn btn-sm btn-outline-info">View details</button>
                </div>
              </div>`);
}

function getDriver(driver) {
  return $('<span><strong>Driver: </strong></span>')
    .append("<span>" + driver.user.lastName + " " + driver.user.firstName + "</span><br>");
}

function getFirstName(driver) {
  return '<span><strong>First name: </strong></span>'
    + '<span>' + driver.user.firstName + '</span><br>';
}

function getLastName(driver) {
  return '<span><strong>Last name: </strong></span>'
    + '<span>' + driver.user.lastName + '</span><br>';
}

function getPhone(driver) {
  return $('<span><strong>Phone: </strong></span>')
    .append("<span>" + driver.user.account.phone + "</span><br>");
}

function getEmail(driver) {
  return $('<span><strong>Email: </strong></span><span>' + driver.user.email + '</span><br><br>');
}

function getCardDetails(driver) {
  const cardDriver = getDriver(driver);
  const cardPhone = getPhone(driver);
  const cardEmail = getEmail(driver);

  return {cardDriver, cardPhone, cardEmail};
}

function createCardBody(cardTitle, driver) {
  const cardBody = $('<div class="card-body"></div>');
  const cardButton = getCardButton(driver);
  const {cardDriver, cardPhone, cardEmail} = getCardDetails(driver);

  cardBody.append(cardTitle)
    .append(cardDriver)
    .append(cardPhone)
    .append(cardEmail)
    .append(cardButton);

  return cardBody;
}

function getApplications() {
  $.post("/controller?command=get_driver_applications", function (data) {
    let $cardContainer = $("#cardContainer");

    $cardContainer.text("");
    data.obj.forEach(myFunction);

    function myFunction(driver) {
      const card = createCard(driver);

      $cardContainer.append(card);
      processFiltering();
    }
  }, "json");
}

function getCar(driver) {
  return '<h5 class="modal-title">' + driver.taxi.carBrand
    + ' ' + driver.taxi.carModel + ' (' + driver.taxi.licencePlate + ')</h5>';
}

function getModalCloseButton() {
  return '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
    + '      <span aria-hidden="true">&times;</span>'
    + '</button>';
}

function getModalButtons(driver) {
  if (driver.driverStatus === "PENDING") {
    return '<button type="button" id="rejectButton" data-id="'
      + driver.user.account.id + '" class="btn btn-outline-danger">Reject</button>'
      + '<button type="button" id="approveButton" data-id="'
      + driver.user.account.id + '" class="btn btn-success">Approve</button>';
  }

  return "";
}

function getBottomCloseButton() {
  return '<button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Close</button>'
}

function addFilterListener() {
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

function addViewButtonListener() {
  $(this).on("click", function (e) {
    const id = e.target?.dataset?.id || null;

    if (id) {
      $.post("/controller?command=get_driver", {id: id}, function (data) {
        console.log(data.obj);
        fillModalWindow(data.obj);
      }, "json");
    }
  });
}

function getDrivingLicence(driver) {
  return $('<span><strong>Driving licence: </strong></span>')
    .append("<span>" + driver.drivingLicence + "</span><br>");
}

function getCoordinate(name, value) {
  return '<span><strong>' + name + ':</strong></span><span> ' + value + ' </span><br>';
}

function clearModalWindow(modalWindow) {
  modalWindow.find("#modalHeader").text("");
  modalWindow.find("#photoContainer").text("");
  modalWindow.find("#infoContainer").text("");
  modalWindow.find("#modalButtons").text("");
}

function fillModalHeader(modalWindow, driver) {
  modalWindow.find("#modalHeader")
    .append(getCar(driver))
    .append(getStatus(driver))
    .append(getModalCloseButton());
}

function fillModalPhoto(modalWindow, driver) {
  modalWindow.find("#photoContainer").append(getCarImage(driver));
}

function fillModalInfo(modalWindow, driver) {
  modalWindow.find("#infoContainer")
    .append("<h3>Driver information</h3>")
    .append(getFirstName(driver))
    .append(getLastName(driver))
    .append(getPhone(driver))
    .append(getDrivingLicence(driver))
    .append(getEmail(driver))
    .append("<hr>")
    .append("<h3>Position</h3>")
    .append(getCoordinate("Latitude", driver.taxi.lastCoordinates.latitude))
    .append(getCoordinate("Longitude", driver.taxi.lastCoordinates.longitude));
}

function fillModalButtons(modalWindow, driver) {
  modalWindow.find("#modalButtons")
    .append(getBottomCloseButton)
    .append(getModalButtons(driver));
}

function fillModalWindow(driver) {
  const modalWindow = $('#driverApplicationModal');

  clearModalWindow(modalWindow);
  fillModalHeader(modalWindow, driver);
  fillModalPhoto(modalWindow, driver);
  fillModalInfo(modalWindow, driver);
  fillModalButtons(modalWindow, driver);

  modalWindow.modal();
  addModalWindowButtonListeners();
}