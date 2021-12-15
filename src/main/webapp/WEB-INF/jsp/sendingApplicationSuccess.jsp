<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.sendingApplicationSuccess" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.checkEmail" var="checkEmailLabel"/>
<fmt:message bundle="${loc}" key="label.furtherInstructions" var="furtherInstructionsLabel"/>
<fmt:message bundle="${loc}" key="label.link.homepage" var="homePageLink"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>${pageTitle}</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/cover/">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
</head>

<body class="text-center">
<div class="jumbotron text-center mb-0">
    <h1 class="display-3">${headerLabel}</h1>
    <p class="lead">
        <strong>${checkEmailLabel}</strong> ${furtherInstructionsLabel}
    </p>
    <hr>
    <p class="lead">
        <a class="btn btn-primary btn-sm" href="${contextPath}/controller?command=main" role="button">
            ${homePageLink}
        </a>
    </p>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
