$(document).ready(function () {
  addNameValidator();
  addEmailValidator();
});

function showValidFeedback($field, $fieldFeedback) {
  $field.addClass("is-valid");
  $field.removeClass("is-invalid");
  $fieldFeedback.hide("slow");
}

function showInvalidFeedback($field, $fieldFeedback, message) {
  $field.addClass("is-invalid");
  $field.removeClass("is-valid");
  $fieldFeedback.text(message);
  $fieldFeedback.show("slow");
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

function addNameValidator() {
  const $firstNameFeedback = $("#firstNameFeedback");
  const $firstName = $("#firstName");
  const nameRegex = new RegExp('^[A-zА-я]{2,255}$');
  const $lastNameFeedback = $("#lastNameFeedback");
  const $lastName = $("#lastName");

  $firstName.on("input", function () {
    checkField($firstName, nameRegex, $firstNameFeedback, "Valid first name is required");
    setButtonState();
  });
  $lastName.on("input", function () {
    checkField($lastName, nameRegex, $lastNameFeedback, "Valid last name is required");
    setButtonState();
  });
}

function setButtonState() {
  const state = $("#firstName").hasClass("is-invalid")
    || $("#lastName").hasClass("is-invalid")
    || $("#email").hasClass("is-invalid");

  $("#submitButton").attr("disabled", state);
}

function addEmailValidator() {
  const $emailFeedback = $("#emailFeedback");
  const $email = $("#email");
  const emailRegex = new RegExp('^([\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4})?$');

  $email.on("input", function () {
    checkField($email, emailRegex, $emailFeedback, "Valid email is required");
    setButtonState();
  });
}
