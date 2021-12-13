<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.driverRegister" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.description" var="descriptionLabel"/>
<fmt:message bundle="${loc}" key="label.header.driverInfo" var="driverInfoLabel"/>
<fmt:message bundle="${loc}" key="label.firstName" var="firstNameLabel"/>
<fmt:message bundle="${loc}" key="label.lastName" var="lastNameLabel"/>
<fmt:message bundle="${loc}" key="label.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.password" var="passwordLabel"/>
<fmt:message bundle="${loc}" key="label.repeatPassword" var="repeatPasswordLabel"/>
<fmt:message bundle="${loc}" key="label.email" var="emailLabel"/>
<fmt:message bundle="${loc}" key="label.drivingSerialNumber" var="drivingSerialNumberLabel"/>
<fmt:message bundle="${loc}" key="label.header.taxiInfo" var="taxiInfoLabel"/>
<fmt:message bundle="${loc}" key="label.carBrand" var="carBrandLabel"/>
<fmt:message bundle="${loc}" key="label.carModel" var="carModelLabel"/>
<fmt:message bundle="${loc}" key="label.carLicencePlate" var="carLicencePlateLabel"/>
<fmt:message bundle="${loc}" key="label.carPhoto" var="carPhotoLabel"/>
<fmt:message bundle="${loc}" key="label.carPhoto.placeholder" var="carPhotoPlaceholderLabel"/>
<fmt:message bundle="${loc}" key="label.button.carPhoto" var="carPhotoButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link href="${contextPath}/css/register.css?v=1.0" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/driverRegister.js?v=1.0"></script>
</head>

<body class="bg-light m-md-5">
<img class="taxi-above d-block mx-auto" id="taxi-above" src="${contextPath}/images/taxi-above.png"
     alt="taxi above" width="550" height="500">
<div class="container">
    <div class="py-5 text-left">
        <h1>${headerLabel}</h1>
        <p class="lead">${descriptionLabel}</p>
    </div>
    <div class="row">
        <div class="col-md-8 order-md-1">
            <h4 class="mb-3">${driverInfoLabel}</h4>
            <form id="registerForm" action="${contextPath}/controller?command=driver_application"
                  class="needs-validation" enctype="multipart/form-data" novalidate>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName">${firstNameLabel}</label>
                        <input type="text" name="fName" class="form-control" id="firstName" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName">${lastNameLabel}</label>
                        <input type="text" name="lName" class="form-control" id="lastName" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="phone">${phoneLabel}</label>
                    <div class="input-group">
                        <input type="tel" class="form-control" name="phone" id="phone" placeholder="+375 29 XXX-XX-XX"
                               required>
                        <div class="invalid-feedback" style="width: 100%;"></div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="password">${passwordLabel}</label>
                        <input type="password" class="form-control" name="password" id="password"
                               value="" required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="passwordRepeat">${repeatPasswordLabel}</label>
                        <input type="password" class="form-control" name="passwordRepeat" id="passwordRepeat"
                               value="" required>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="email">${emailLabel}</label>
                    <input type="email" class="form-control" name="email" id="email" placeholder="you@example.com">
                    <div class="invalid-feedback"></div>
                </div>

                <div class="mb-3">
                    <label for="drivingLicence">${drivingSerialNumberLabel}</label>
                    <input type="text" class="form-control" name="drivingLicence" id="drivingLicence"
                           placeholder="1GE 160135" required>
                    <div class="invalid-feedback"></div>
                </div>

                <br>
                <hr class="mb-4">
                <h4 class="mb-3">${taxiInfoLabel}</h4>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="brand">${carBrandLabel}</label>
                        <input type="text" class="form-control" name="brand" id="brand" placeholder="Volkswagen"
                               value="" required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="model">${carModelLabel}</label>
                        <input type="text" class="form-control" name="model" id="model" placeholder="Polo" value=""
                               required>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="licencePlate">${carLicencePlateLabel}</label>
                    <input type="text" class="form-control" name="licencePlate" id="licencePlate"
                           placeholder="1111 AX-7" required>
                    <div class="invalid-feedback"></div>
                </div>
                <div class="mb-3">
                    <input type="file" name="carPhoto" class="file" accept="image/*" hidden>
                    <label for="carPhoto">${carPhotoLabel}</label>
                    <div class="input-group">
                        <input type="text" class="form-control" disabled placeholder="${carPhotoPlaceholderLabel}"
                               id="carPhoto">
                        <div id="browseButton" class="input-group-append">
                            <button type="button" class="browse btn btn-primary">${carPhotoButtonLabel}</button>
                        </div>
                        <div class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="mb-3">
                    <img src="" id="preview" class="img-thumbnail">
                </div>
                <hr class="mb-4">
                <button class="btn btn-primary btn-lg btn-block" type="submit">${submitButtonLabel}</button>
            </form>
        </div>
    </div>
</div>
<script>
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
</script>
</body>
</html>
