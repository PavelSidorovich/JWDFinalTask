<%@ page isErrorPage="true" contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="l10n.page.error" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.page.message" var="errorMsgLabel"/>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>${pageTitle}</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/error.css" rel="stylesheet">
</head>

<div class="d-flex justify-content-center align-items-center" id="main">
    <h1 class="mr-3 pr-3 align-top border-right inline-block align-content-center">${pageContext.errorData.statusCode}</h1>
    <div class="inline-block align-middle">
        <c:choose>
            <c:when test="${not empty requestScope.errorPageMessage}">
                <h2 class="font-weight-normal lead" id="desc">${requestScope.errorPageMessage}</h2>
            </c:when>
            <c:otherwise>
                <h2 class="font-weight-normal lead" id="desc">${errorMsgLabel}</h2>
            </c:otherwise>
        </c:choose>
    </div>
</div>
