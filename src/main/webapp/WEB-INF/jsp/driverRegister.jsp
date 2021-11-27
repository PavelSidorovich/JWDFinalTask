<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Becoming a driver</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <link href="${contextPath}/css/register.css?v=1.0" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/validator.js"></script>
</head>

<body class="bg-light m-md-5">
<img class="taxi-above d-block mx-auto" id="taxi-above" src="${contextPath}/images/taxi-above.png"
     alt="taxi above" width="500" height="500">
<div class="container">

    <div class="py-5 text-left">
        <h1>Become a driver</h1>
        <p class="lead">Working with Buber.Taxi partners is the freedom to manage your time and income</p>
    </div>

    <div class="row">
        <div class="col-md-8 order-md-1">
            <h4 class="mb-3">Driver personal information</h4>
            <form id="registerForm" action="${contextPath}/controller?command=driver_application"
                  class="needs-validation" enctype="multipart/form-data" novalidate>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName">First name</label>
                        <input type="text" name="fName" class="form-control" id="firstName" placeholder="" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName">Last name</label>
                        <input type="text" name="lName" class="form-control" id="lastName" placeholder="" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="phone">Phone</label>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">+</span>
                        </div>
                        <input type="tel" class="form-control" name="phone" id="phone" placeholder="375 29 XXX-XX-XX"
                               required>
                        <div class="invalid-feedback" style="width: 100%;"></div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" name="password" id="password" placeholder=""
                               value="" required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="passwordRepeat">Repeat password</label>
                        <input type="password" class="form-control" name="passwordRepeat" id="passwordRepeat"
                               placeholder="" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="email">Email <span class="text-muted">(Optional)</span></label>
                    <input type="email" class="form-control" name="email" id="email" placeholder="you@example.com">
                    <div class="invalid-feedback"></div>
                </div>

                <div class="mb-3">
                    <label for="drivingLicence">Driving licence serial number</label>
                    <input type="text" class="form-control" name="drivingLicence" id="drivingLicence"
                           placeholder="1GE 160135" required>
                    <div class="invalid-feedback"></div>
                </div>

                <br>
                <hr class="mb-4">
                <h4 class="mb-3">Taxi information</h4>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="brand">Car brand</label>
                        <input type="text" class="form-control" name="brand" id="brand" placeholder="Volkswagen"
                               value="" required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="model">Car model</label>
                        <input type="text" class="form-control" name="model" id="model" placeholder="Polo" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="licencePlate">Car licence plate</label>
                    <input type="text" class="form-control" name="licencePlate" id="licencePlate"
                           placeholder="1111 AX-7" required>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="mb-3">
                    <input type="file" name="carPhoto" class="file" accept="image/*">
                    <label for="carPhoto">Car photo</label>
                    <div class="input-group">
                        <input type="text" class="form-control" disabled placeholder="Upload File" id="carPhoto">
                        <div id="browseButton" class="input-group-append">
                            <button type="button" class="browse btn btn-primary">Browse...</button>
                        </div>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="mb-3">
                    <img src="" id="preview" class="img-thumbnail">
                </div>
                <hr class="mb-4">
                <button class="btn btn-primary btn-lg btn-block" type="submit">Send an application</button>
            </form>
        </div>
    </div>
</div>

<script>
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
        } else if(data.isRedirect) {
          window.location.replace(data.path);
        }
      },
      error: function (data) {
        console.log("error");
        console.log(data);
      }
    });
  }));

  $(document).on("click", ".browse", function () {
    let file = $(this).parents().find(".file");
    file.trigger("click");
  });
  $('input[type="file"]').change(function (e) {
    let fileName = e.target.files[0].name;
    $("#carPhoto").val(fileName);

    let reader = new FileReader();
    reader.onload = function (e) {
      // get loaded data and render thumbnail.
      document.getElementById("preview").src = e.target.result;
    };
    // read the image file as a data URL.
    reader.readAsDataURL(this.files[0]);
  });

  function showErrorMessage($firstName, error) {
    let errorDiv = $firstName.next('div.invalid-feedback');

    errorDiv.text(error);
    errorDiv.show("slow");
  }

  function showErrorMessages(errorsByMessages){
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
    if (errorsByMessages.phone) {
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
    console.log(errorsByMessages);
  }
</script>
</body>

</html>
