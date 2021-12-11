$(document).ready(function () {
  addPasswordValidator();
});

function checkField($field, regex, $fieldFeedback, message) {
  if ($field.val().match(regex)) {
    showValidFeedback($field, $fieldFeedback);

    return true;
  } else {
    showInvalidFeedback($field, $fieldFeedback, message);

    return false;
  }
}

function addPasswordValidator() {
  const $passwordFeedback = $("#passwordFeedback");
  const $password = $("#password");
  const passwordRegex = new RegExp('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$');
  const $passwordRepeatFeedback = $("#passwordRepeatFeedback");
  const $passwordRepeat = $("#passwordRepeat");

  $password.on("input", function () {
    checkField($password, passwordRegex, $passwordFeedback, passwordFeedbackMsg);
    checkField($passwordRepeat, "^" + $password.val() + "$", $passwordRepeatFeedback,
      passwordRepeatFeedbackMsg);
    setButtonState();
  });
  $passwordRepeat.on("input", function () {
    checkField($passwordRepeat, "^" + $password.val() + "$", $passwordRepeatFeedback,
      passwordRepeatFeedbackMsg);
    setButtonState();
  });
}

function setButtonState() {
  const state = $("#password").hasClass("is-invalid")
    || $("#passwordRepeat").hasClass("is-invalid");

  $("#editPasswordButton").attr("disabled", state);
}
