$(document).ready(function () {
  addRegisterFormListener();
  addNameValidator();
  addEmailValidator();
  addPhoneValidator();
  addPasswordValidator();
  addPasswordRepeatValidator();
});

function addRegisterFormListener() {
  $("#registerForm").submit(function (event) {
    event.preventDefault();
    $.post(
      $(this).attr("action"),
      $(this).serialize(),
      function (data) {
        if (data.status === "ERROR") {
          showErrorMessages(data.obj);
          console.log(data.obj);
        } else if (data.isRedirect) {
          window.location.replace(data.path);
        }
      });
  });
}

function showErrorMessage($field, errorMsg) {
  let errorDiv = $field.attr('aria-describedby');
  let $invalidFeedback = $("#" + errorDiv);

  $invalidFeedback.text(errorMsg);
  $invalidFeedback.show("slow");
}

function showErrorMessages(errorsByMessages) {
  $('div.invalid-feedback').hide();
  if (errorsByMessages.fName) {
    showErrorMessage($("#firstName"), errorsByMessages.fName);
  }
  if (errorsByMessages.lName) {
    showErrorMessage($("#lastName"), errorsByMessages.lName);
  }
  if (errorsByMessages.phone) {
    showErrorMessage($("#phone"), errorsByMessages.phone);
  }
  if (errorsByMessages.password) {
    showErrorMessage($("#password"), errorsByMessages.password);
  }
  if (errorsByMessages.passwordRepeat) {
    showErrorMessage($("#passwordRepeat"), errorsByMessages.passwordRepeat);
  }
  if (errorsByMessages.email) {
    showErrorMessage($("#inputEmail"), errorsByMessages.email);
  }
}

function showValidFeedback($field) {
  $field.addClass("is-valid");
  $field.removeClass("is-invalid");
  $("#" + $field.attr("aria-describedby")).hide("slow");
}

function showInvalidFeedback($field) {
  $field.addClass("is-invalid");
  $field.removeClass("is-valid");
}

function checkField($field, regex) {
  if ($field.val().match(regex)) {
    showValidFeedback($field);

    return true;
  } else {
    showInvalidFeedback($field);

    return false;
  }
}

function addNameValidator() {
  const $firstName = $("#firstName");
  const nameRegex = new RegExp('^[A-zА-я]{2,255}$');
  const $lastName = $("#lastName");

  $firstName.on("input", function () {
    checkField($firstName, nameRegex);
    setSaveButtonState();
  });
  $lastName.on("input", function () {
    checkField($lastName, nameRegex);
    setSaveButtonState();
  });
}

function addEmailValidator() {
  const $email = $("#inputEmail");
  const emailRegex = new RegExp('^([\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4})?$');

  $email.on("input", function () {
    checkField($email, emailRegex);
  });
}

function addPhoneValidator() {
  const $phone = $("#phone");
  const phoneRegex = new RegExp('^[+]375[ ]\\d{2}[ ]\\d{3}[-]\\d{2}[-]\\d{2}$');

  $phone.on("input", function () {
    checkField($phone, phoneRegex);
  });
}

function addPasswordValidator() {
  const $password = $("#password");
  const passwordRegex = new RegExp('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,255}$');
  const $passwordRepeat = $("#passwordRepeat");

  $password.on("input", function () {
    checkField($password, passwordRegex);
    checkField($passwordRepeat, "^" + $password.val() + "$");
  });
}

function addPasswordRepeatValidator() {
  const $passwordRepeat = $("#passwordRepeat");

  $passwordRepeat.on("input", function () {
    checkField($passwordRepeat, "^" + $("#password").val() + "$");
  });
}
