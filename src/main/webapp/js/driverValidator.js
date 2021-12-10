$(document).ready(function () {
  addDrivingLicenceValidator();
  addCarNameValidator();
  addCarLicencePlateValidator();
  setSaveButtonState();
});

function showInvalidFeedback($field, $fieldFeedback, message) {
  $field.addClass("is-invalid");
  $field.removeClass("is-valid");
  $fieldFeedback.text(message);
  $fieldFeedback.show("slow");
}

function showValidFeedback($field, $fieldFeedback) {
  $field.addClass("is-valid");
  $field.removeClass("is-invalid");
  $fieldFeedback.hide("slow");
}

function checkField($field, regex, $fieldFeedback, message) {
  if ($field.val().match(regex)) {
    showValidFeedback($field, $fieldFeedback);

    return true;
  } else {
    showInvalidFeedback($field, $fieldFeedback, message);

    return false;
  }
}

function addDrivingLicenceValidator() {
  const $drivingLicenceSerial = $("#drivingLicenceSerial");
  const $drivingLicenceSerialFeedback = $("#drivingLicenceSerialFeedback");
  const drivingLicenceRegex = new RegExp('^\\d[A-Z]{2} \\d{6}$');

  $drivingLicenceSerial.on("input", function () {
    checkField($drivingLicenceSerial, drivingLicenceRegex, $drivingLicenceSerialFeedback, "Valid driving licence serial number is required");
    setSaveButtonState();
  });
}

function addCarNameValidator() {
  const $carBrand = $("#carBrand");
  const $carModel = $("#carModel");
  const $carBrandFeedback = $("#carBrandFeedback");
  const $carModelFeedback = $("#carModelFeedback");
  const carNameRegex = new RegExp('^[A-zА-Я0-9 ]{2,255}$');

  $carBrand.on("input", function () {
    checkField($carBrand, carNameRegex, $carBrandFeedback, "Valid car brand is required");
    setSaveButtonState();
  });
  $carModel.on("input", function () {
    checkField($carModel, carNameRegex, $carModelFeedback, "Valid car model is required");
    setSaveButtonState();
  });
}

function addCarLicencePlateValidator() {
  const $carLicencePlate = $("#carLicencePlate");
  const $carLicencePlateFeedback = $("#carLicencePlateFeedback");
  const carLicencePlateRegex = new RegExp('^\\d{4} [A-Z]{2}-[1-7]$');

  $carLicencePlate.on("input", function () {
    checkField($carLicencePlate, carLicencePlateRegex, $carLicencePlateFeedback, "Valid car licence plate is required");
    setSaveButtonState();
  });
}

function setSaveButtonState() {
  const state = $("#carBrand").hasClass("is-invalid")
    || $("#carModel").hasClass("is-invalid")
    || $("#carLicencePlate").hasClass("is-invalid")
    || $("#drivingLicenceSerial").hasClass("is-invalid");

  console.log(state);

  $("#submitButton").attr("disabled", state);
}
