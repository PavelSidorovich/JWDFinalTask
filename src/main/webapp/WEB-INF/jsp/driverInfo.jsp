<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.DriverStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="driver" scope="request" type="com.sidorovich.pavel.buber.api.model.Driver"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.driverInfo" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.alt.taxiPhoto" var="taxiPhotoLabel"/>
<fmt:message bundle="${loc}" key="label.header.driverInfo" var="driverInfoLabel"/>
<fmt:message bundle="${loc}" key="label.header.status" var="driverStatusLabel"/>
<fmt:message bundle="${loc}" key="label.status.pending" var="pendingLabel"/>
<fmt:message bundle="${loc}" key="label.status.rejected" var="rejectedLabel"/>
<fmt:message bundle="${loc}" key="label.status.busy" var="busyLabel"/>
<fmt:message bundle="${loc}" key="label.status.free" var="freeLabel"/>
<fmt:message bundle="${loc}" key="label.status.rest" var="restLabel"/>
<fmt:message bundle="${loc}" key="label.header.drivingLicence" var="drivingLicenceLabel"/>
<fmt:message bundle="${loc}" key="label.header.taxiInfo" var="taxiInfoLabel"/>
<fmt:message bundle="${loc}" key="label.header.carBrand" var="carBrandLabel"/>
<fmt:message bundle="${loc}" key="label.header.carModel" var="carModelLabel"/>
<fmt:message bundle="${loc}" key="label.header.carLicencePlate" var="carLicencePlateLabel"/>
<fmt:message bundle="${loc}" key="label.alert.danger" var="alertDangerLabel"/>
<fmt:message bundle="${loc}" key="label.alert.info.strong" var="infoStrongLabel"/>
<fmt:message bundle="${loc}" key="label.alert.info.message" var="infoMessageLabel"/>
<fmt:message bundle="${loc}" key="label.button.fillForm" var="fillFormButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.editDriverInfo" var="editDriverInfoButtonLabel"/>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
</head>

<body>
<jsp:include page="partials/driverNavBar.jsp"/>
<div class="container my-3">
    <div class="row">
        <c:if test="${not empty driver}">
            <div class="col-md-7">
                <img src="${contextPath}/images/taxes/${driver.taxi.photoFilepath}" class="img-fluid" alt="${taxiPhotoLabel}">
            </div>
            <div class="col-md-5">
                <h3 class="mb-3">${driverInfoLabel}</h3>
                <div class="mb-3 pl-3">
                    <c:choose>
                        <c:when test="${driver.driverStatus eq DriverStatus.PENDING}">
                            <h6>${driverStatusLabel} <span class="badge badge-pill badge-warning">${pendingLabel}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.REJECTED}">
                            <h6>${driverStatusLabel} <span class="badge badge-pill badge-danger">${rejectedLabel}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.BUSY}">
                            <h6>${driverStatusLabel} <span class="badge badge-pill badge-success">${busyLabel}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.FREE}">
                            <h6>${driverStatusLabel} <span class="badge badge-pill badge-primary">${freeLabel}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.REST}">
                            <h6>${driverStatusLabel} <span class="badge badge-pill badge-info">${restLabel}</span></h6>
                        </c:when>
                    </c:choose>
                </div>
                <div class="mb-3 pl-3">
                    <h6>${drivingLicenceLabel} <span class="text-muted">${driver.drivingLicence}</span></h6>
                </div>
                <hr class="mb-4">
                <h3 class="mb-3">${taxiInfoLabel}</h3>
                <div class="mb-3 pl-3">
                    <h6>${carBrandLabel} <span class="text-muted">${driver.taxi.carBrand}</span></h6>
                </div>
                <div class="mb-3 pl-3">
                    <h6>${carModelLabel} <span class="text-muted">${driver.taxi.carModel}</span></h6>
                </div>
                <div class="mb-3 pl-3">
                    <h6>${carLicencePlateLabel} <span class="text-muted">${driver.taxi.licencePlate}</span></h6>
                </div>
                <br>
                <c:choose>
                    <c:when test="${driver.driverStatus eq DriverStatus.BUSY or driver.driverStatus eq DriverStatus.FREE}">
                        <div class="alert alert-danger" role="alert">
                            ${alertDangerLabel}
                        </div>
                    </c:when>
                    <c:when test="${driver.driverStatus eq DriverStatus.REJECTED}">
                        <div class="alert alert-info" role="alert">
                            <strong>${infoStrongLabel}</strong> ${infoMessageLabel}                            details.
                        </div>
                        <button type="button" class="btn btn-block btn-warning" data-toggle="modal"
                                data-target="#driverInfoModal">${fillFormButtonLabel}
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-block btn-warning" data-toggle="modal"
                                data-target="#driverInfoModal">${editDriverInfoButtonLabel}
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
</div>
<jsp:include page="partials/driverInfoModal.jsp"/>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
