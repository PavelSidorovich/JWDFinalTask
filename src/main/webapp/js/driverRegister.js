$(document).ready(function () {
  addDriverRegisterFormSubmitListener();
  addNameValidator();
  addEmailValidator();
  addPhoneValidator();
  addPasswordValidator();
  addPasswordRepeatValidator();
  addDrivingLicenceValidator();
  addCarNameValidator();
  addCarLicencePlateValidator();
});

function addDriverRegisterFormSubmitListener() {
  $('#registerForm').on("submit", (function (e) {
    e.preventDefault();
    $.ajax({
      type: 'POST',
      url: $(this).attr('action'),
      data: new FormData(this),
      cache: false,
      contentType: false,
      processData: false,
      enctype: "multipart/form-data",
      success: function (data) {
        if (data.status === "ERROR") {
          showErrorMessages(data.obj);
        } else if (data.isRedirect) {
          window.location.replace(data.path);
        }
      },
      error: function (data) {
        console.log("error");
        console.log(data);
      }
    });
  }));
}

function showErrorMessage($firstName, error) {
  let errorDiv = $firstName.next('div.invalid-feedback');

  errorDiv.text(error);
  errorDiv.show("slow");
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
  if (errorsByMessages.drivingLicence) {
    showErrorMessage($("#drivingLicence"), errorsByMessages.drivingLicence);
  }
  if (errorsByMessages.carBrand) {
    showErrorMessage($("#brand"), errorsByMessages.carBrand);
  }
  if (errorsByMessages.carModel) {
    showErrorMessage($("#model"), errorsByMessages.carModel);
  }
  if (errorsByMessages.licencePlate) {
    showErrorMessage($("#licencePlate"), errorsByMessages.licencePlate);
  }
  if (errorsByMessages.carPhoto) {
    showErrorMessage($("#browseButton"), errorsByMessages.carPhoto);
  }
  if (errorsByMessages.email) {
    showErrorMessage($("#email"), errorsByMessages.email);
  }
  console.log(errorsByMessages);
}

function showInvalidFeedback($field) {
  $field.addClass("is-invalid");
  $field.removeClass("is-valid");
}

function showValidFeedback($field) {
  $field.addClass("is-valid");
  $field.removeClass("is-invalid");
  $("#" + $field.attr("aria-describedby")).hide("slow");
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
  const $email = $("#email");
  const emailRegex = new RegExp('^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$');

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

function addDrivingLicenceValidator() {
  const $drivingLicence = $("#drivingLicence");
  const drivingLicenceRegex = new RegExp('^\\d[A-Z]{2} \\d{6}$');

  $drivingLicence.on("input", function () {
    checkField($drivingLicence, drivingLicenceRegex);
  });
}

function addCarNameValidator() {
  const $carBrand = $("#brand");
  const $carModel = $("#model");
  const carNameRegex = new RegExp('^[A-zА-Я0-9 ]{2,255}$');

  $carBrand.on("input", function () {
    checkField($carBrand, carNameRegex);
  });
  $carModel.on("input", function () {
    checkField($carModel, carNameRegex);
  });
}

function addCarLicencePlateValidator() {
  const $carLicencePlate = $("#licencePlate");
  const carLicencePlateRegex = new RegExp('^\\d{4} [A-Z]{2}-[1-7]$');

  $carLicencePlate.on("input", function () {
    checkField($carLicencePlate, carLicencePlateRegex);
    setSaveButtonState();
  });
}