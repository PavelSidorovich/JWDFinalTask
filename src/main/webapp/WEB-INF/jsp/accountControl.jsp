<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.Role" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.accountControl" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.link.editPersonalInfo" var="editPersonalInfoLink"/>
<fmt:message bundle="${loc}" key="label.alt.accountPhoto" var="accountPhotoLabel"/>
<fmt:message bundle="${loc}" key="label.link.changePassword" var="changePasswordLink"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
<%--    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">--%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
<%--    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>--%>
    <script src="${contextPath}/js/changePassword.js?v=1.2" type="text/javascript"></script>
    <script src="${contextPath}/js/personalInfo.js?v=1.5" type="text/javascript"></script>
</head>

<body>
<c:if test="${not empty requestScope.user}">
    <c:choose>
        <c:when test="${requestScope.user.account.role eq Role.CLIENT}">
            <jsp:include page="partials/userNavBar.jsp"/>
        </c:when>
        <c:when test="${requestScope.user.account.role eq Role.DRIVER}">
            <jsp:include page="partials/driverNavBar.jsp"/>
        </c:when>
    </c:choose>
</c:if>
<div class="container my-5">
    <div class="card">
        <div class="row no-gutters">
            <div class="col-md-3 my-5 ml-5">
                <img src="${contextPath}/images/person-circle.svg" width="200em" alt="${accountPhotoLabel}">
            </div>
            <div class="col">
                <c:if test="${not empty requestScope.user}">
                    <div class="card-body my-5">
                        <h1 class="card-title">${requestScope.user.firstName} ${requestScope.user.lastName}</h1>
                        <h5 class="card-title">${requestScope.user.account.phone}</h5>
                        <h5 class="card-title">${requestScope.user.email.orElse("")}</h5>
                        <a href="#" class="btn-link" data-toggle="modal" data-target="#personalInfoModal">
                                ${editPersonalInfoLink}
                        </a>
                        <br>
                        <a href="#" class="btn-link" data-toggle="modal" data-target="#editPasswordModal">
                                ${changePasswordLink}
                        </a>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<jsp:include page="partials/editPasswordModal.jsp"/>
<jsp:include page="partials/personalInfoModal.jsp"/>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
