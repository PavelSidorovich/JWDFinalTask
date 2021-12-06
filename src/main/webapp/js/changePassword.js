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
    checkField($password, passwordRegex, $passwordFeedback,
      "Password should contain minimum eight characters, at least one letter and one number");
    checkField($passwordRepeat, "^" + $password.val() + "$", $passwordRepeatFeedback,
      "Passwords are not equal");
    setButtonState();
  });
  $passwordRepeat.on("input", function () {
    checkField($passwordRepeat, "^" + $password.val() + "$", $passwordRepeatFeedback,
      "Passwords are not equal");
    setButtonState();
  });
}

function setButtonState() {
  const state = $("#password").hasClass("is-invalid")
    || $("#passwordRepeat").hasClass("is-invalid");

  $("#editPasswordButton").attr("disabled", state);
}
