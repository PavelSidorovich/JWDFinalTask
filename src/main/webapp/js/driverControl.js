$(document).ready(function () {
  getApplications();
  addFilterListener();
  addViewButtonListener();
});

function addListenerToRejectButton() {
  $("#rejectButton").on("click", function (e) {
    sendPostRequest(e, "REJECTED");
  })
}

function addListenerToApproveButton() {
  $("#approveButton").on("click", function (e) {
    sendPostRequest(e, "BUSY");
  })
}

//todo get message from server
function sendPostRequest(e, driverStatus) {
  const id = e.target?.dataset?.id || null;
  if (id) {
    $.post("/controller?command=update_driver_status", {
      id: id,
      driverStatus: driverStatus,
      comment: $("textArea").val()
    })
      .done(function (data) {
        if (data.obj.status === "ERROR") {
          $("#cardContainer").before(createAlert("ERROR",
            failMsg));
        } else {
          $("#cardContainer").before(createAlert("SUCCESS",
            successMsg));
        }
        getApplications();
      }, "json");
  }
  $("#driverApplicationModal").modal("hide");
}

function addModalWindowButtonListeners() {
  addListenerToRejectButton();
  addListenerToApproveButton();
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
    'src="../images/taxes/' + driver.taxi.photoFilepath + '" alt=' + taxiPhotoAlt + '>');
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
  const cardStatus = $('<span></span>');

  if (driver.driverStatus === "PENDING") {
    cardStatus.text(pendingLabel);
    cardStatus.attr("class", "badge badge-pill badge-warning");
  } else if (driver.driverStatus === "REJECTED") {
    cardStatus.text(rejectedLabel);
    cardStatus.attr("class", "badge badge-pill badge-danger");
  } else if (driver.driverStatus === "FREE") {
    cardStatus.text(freeLabel);
    cardStatus.attr("class", "badge badge-pill badge-success");
  } else if (driver.driverStatus === "BUSY") {
    cardStatus.text(busyLabel);
    cardStatus.attr("class", "badge badge-pill badge-success");
  } else if (driver.driverStatus === "REST") {
    cardStatus.text(restLabel);
    cardStatus.attr("class", "badge badge-pill badge-info");
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
                  class="btn btn-sm btn-outline-info">` + detailsButtonLabel + `</button>
                </div>
              </div>`);
}

function getDriver(driver) {
  return $('<span><strong>' + driverLabel + ' </strong></span>')
    .append("<span>" + driver.user.lastName + " " + driver.user.firstName + "</span><br>");
}

function getFirstName(driver) {
  return '<span><strong>' + fNameLabel + ' </strong></span>'
    + '<span>' + driver.user.firstName + '</span><br>';
}

function getLastName(driver) {
  return '<span><strong>' + lNameLabel + ' </strong></span>'
    + '<span>' + driver.user.lastName + '</span><br>';
}

function getPhone(driver) {
  return $('<span><strong>' + phoneLabel + ' </strong></span>')
    .append("<span>" + driver.user.account.phone + "</span><br>");
}

function getEmail(driver) {
  return $('<span><strong>' + emailLabel + ' </strong></span><span>' + driver.user.email + '</span><br><br>');
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
      + driver.user.account.id + '" class="btn btn-outline-danger">' + rejectButtonLabel + '</button>'
      + '<button type="button" id="approveButton" data-id="'
      + driver.user.account.id + '" class="btn btn-success">' + approveButtonLabel + '</button>';
  }

  return "";
}

function getBottomCloseButton() {
  return '<button type="button" class="btn btn-outline-secondary" data-dismiss="modal">' + closeButtonLabel + '</button>'
}

function addFilterListener() {
  $("#searchInput").on("keyup", function () {
    processFiltering();
  });
}

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
  return $('<span><strong>' + drivingLicenceLabel + ' </strong></span>')
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
  modalWindow.find("textarea").val("");
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
    .append("<h3>" + driverInfoLabel + "</h3>")
    .append(getFirstName(driver))
    .append(getLastName(driver))
    .append(getPhone(driver))
    .append(getDrivingLicence(driver))
    .append(getEmail(driver))
    .append("<hr>")
    .append("<h3>" + positionLabel + "</h3>")
    .append(getCoordinate(latitudeLabel, driver.taxi.lastCoordinates.latitude))
    .append(getCoordinate(longitudeLabel, driver.taxi.lastCoordinates.longitude));
}

function fillModalButtons(modalWindow, driver) {
  modalWindow.find("#modalButtons")
    .append(getBottomCloseButton)
    .append(getModalButtons(driver));
}

function hideOrShowComment(driver) {
  $("#commentBlock").toggle(driver.driverStatus === "PENDING")
}

function fillModalWindow(driver) {
  const modalWindow = $('#driverApplicationModal');

  clearModalWindow(modalWindow);
  fillModalHeader(modalWindow, driver);
  fillModalPhoto(modalWindow, driver);
  fillModalInfo(modalWindow, driver);
  fillModalButtons(modalWindow, driver);
  hideOrShowComment(driver);

  modalWindow.modal();
  addModalWindowButtonListeners();
}