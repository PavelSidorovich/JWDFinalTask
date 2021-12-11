<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setBundle basename="l10n.page.login" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.signIn" var="signInLabel"/>
<fmt:message bundle="${loc}" key="label.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.placeholder.phone" var="phonePlaceholderLabel"/>
<fmt:message bundle="${loc}" key="label.password" var="passwordLabel"/>
<fmt:message bundle="${loc}" key="label.placeholder.password" var="passwordPlaceholderLabel"/>
<fmt:message bundle="${loc}" key="label.button.signIn" var="signInButtonLabel"/>
<fmt:message bundle="${loc}" key="label.noAccount" var="noAccountLabel"/>
<fmt:message bundle="${loc}" key="label.link.signUp" var="signUpLabel"/>
<fmt:message bundle="${loc}" key="label.feedback" var="invalidFeedbackLabel"/>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/signIn.css?v=1.1" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/login.js?v=1.1"></script>
</head>

<body class="text-center">
<form id="loginForm" class="form-signIn" action="${contextPath}/controller?command=login" novalidate>
    <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72"
         height="72">
    <h1 class="h3 mb-3 font-weight-normal">${signInLabel}</h1>
    <label for="inputPhone" class="sr-only">${phoneLabel}</label>
    <input type="tel" name="phone" id="inputPhone" class="form-control" placeholder="${phonePlaceholderLabel}"
           autofocus>
    <br>
    <label for="inputPassword" class="sr-only">${passwordLabel}</label>
    <input type="password" name="password" id="inputPassword" class="form-control"
           placeholder="${passwordPlaceholderLabel}">
    <br>
    <b id="errorLoginMsg"></b>
    <br><br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">${signInButtonLabel}</button>
    <br>
    <p>${noAccountLabel}</p>
    <a class="btn btn-outline-secondary btn-block" href="${contextPath}/controller?command=show_user_register">
        ${signUpLabel}
    </a>
    <p class="mt-5 mb-3 text-muted">© Copyright 2021-2021 by BuberTaxi.</p>
</form>
<script>
  const invalidLoginOrPassword = '${invalidFeedbackLabel}';
</script>
</body>
</html>
