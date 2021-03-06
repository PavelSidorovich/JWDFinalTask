<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.register" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.firstName" var="firstNameLabel"/>
<fmt:message bundle="${loc}" key="label.lastName" var="lastNameLabel"/>
<fmt:message bundle="${loc}" key="label.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.password" var="passwordLabel"/>
<fmt:message bundle="${loc}" key="label.repeatPassword" var="repeatPasswordLabel"/>
<fmt:message bundle="${loc}" key="label.email" var="emailLabel"/>
<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>
<fmt:message bundle="${loc}" key="label.haveAccount" var="haveAccLabel"/>
<fmt:message bundle="${loc}" key="label.link.signIn" var="signInLink"/>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <link href="${contextPath}/css/register.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/register.js?v=1.1"></script>
</head>

<body class="text-center">
<form id="registerForm" class="form-register" action="${contextPath}/controller?command=user_register" novalidate>
    <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72"
         height="72">
    <h1 class="h3 mb-3 font-weight-normal">${headerLabel}</h1>
    <label for="firstName" class="sr-only">${firstNameLabel}</label>
    <input type="text" name="fName" id="firstName" class="form-control" placeholder="${firstNameLabel}"
           aria-describedby="firstNameFeedback" autofocus>
    <div id="firstNameFeedback" class="invalid-feedback"></div>
    <br>
    <label for="lastName" class="sr-only">${lastNameLabel}</label>
    <input type="text" name="lName" id="lastName" class="form-control" placeholder="${lastNameLabel}"
           aria-describedby="lastNameFeedback">
    <div id="lastNameFeedback" class="invalid-feedback"></div>
    <br>
    <label for="phone" class="sr-only">${phoneLabel}</label>
    <input type="tel" name="phone" id="phone" class="form-control" placeholder="${phoneLabel}"
           aria-describedby="phoneFeedback">
    <div id="phoneFeedback" class="invalid-feedback"></div>
    <br>
    <label for="password" class="sr-only">${passwordLabel}</label>
    <input type="password" name="password" id="password" class="form-control" placeholder="${passwordLabel}"
           aria-describedby="passwordFeedback">
    <div id="passwordFeedback" class="invalid-feedback"></div>
    <br>
    <label for="passwordRepeat" class="sr-only">${repeatPasswordLabel}</label>
    <input type="password" name="passwordRepeat" id="passwordRepeat" class="form-control"
           placeholder="${repeatPasswordLabel}" aria-describedby="passwordRepeatFeedback">
    <div id="passwordRepeatFeedback" class="invalid-feedback"></div>
    <br>
    <label for="inputEmail" class="sr-only">${emailLabel}</label>
    <input type="email" name="email" id="inputEmail" class="form-control" placeholder="${emailLabel}"
           aria-describedby="emailFeedback">
    <div id="emailFeedback" class="invalid-feedback"></div>
    <br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">${submitButtonLabel}</button>
    <br>
    <p>${haveAccLabel}</p>
    <a class="btn btn-outline-dark btn-block" href="${contextPath}/controller?command=show_login">
        ${signInLink}
    </a>
    <p class="mt-5 mb-3 text-muted">?? Copyright 2021-2021 by BuberTaxi.</p>
</form>
</body>
</html>
