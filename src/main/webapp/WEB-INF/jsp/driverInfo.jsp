<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.DriverStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="driver" scope="request" type="com.sidorovich.pavel.buber.api.model.Driver"/>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Driver info</title>
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
                <img src="../../images/taxes/${driver.taxi.photoFilepath}" class="img-fluid" alt="taxi photo">
            </div>
            <div class="col-md-5">
                <h3 class="mb-3">Driver info</h3>
                <div class="mb-3 pl-3">
                    <c:choose>
                        <c:when test="${driver.driverStatus eq DriverStatus.PENDING}">
                            <h6>Status: <span class="badge badge-pill badge-warning">${driver.driverStatus}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.REJECTED}">
                            <h6>Status: <span class="badge badge-pill badge-danger">${driver.driverStatus}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.BUSY}">
                            <h6>Status: <span class="badge badge-pill badge-success">${driver.driverStatus}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.FREE}">
                            <h6>Status: <span class="badge badge-pill badge-primary">${driver.driverStatus}</span></h6>
                        </c:when>
                        <c:when test="${driver.driverStatus eq DriverStatus.REST}">
                            <h6>Status: <span class="badge badge-pill badge-info">${driver.driverStatus}</span></h6>
                        </c:when>
                    </c:choose>
                </div>
                <div class="mb-3 pl-3">
                    <h6>Driving licence serial number: <span class="text-muted">${driver.drivingLicence}</span></h6>
                </div>
                <hr class="mb-4">
                <h3 class="mb-3">Taxi</h3>
                <div class="mb-3 pl-3">
                    <h6>Car brand: <span class="text-muted">${driver.taxi.carBrand}</span></h6>
                </div>
                <div class="mb-3 pl-3">
                    <h6>Car model: <span class="text-muted">${driver.taxi.carModel}</span></h6>
                </div>
                <div class="mb-3 pl-3">
                    <h6>Car licence plate: <span class="text-muted">${driver.taxi.licencePlate}</span></h6>
                </div>
                <br>
                <c:choose>
                    <c:when test="${driver.driverStatus eq DriverStatus.BUSY or driver.driverStatus eq DriverStatus.FREE}">
                        <div class="alert alert-danger" role="alert">
                            Editing driver info while waiting for orders or processing order is impossible
                        </div>
                    </c:when>
                    <c:when test="${driver.driverStatus eq DriverStatus.REJECTED}">
                        <div class="alert alert-info" role="alert">
                            <strong>Information:</strong> Your application was rejected. Please check your email for
                            details.
                        </div>
                        <button type="button" class="btn btn-block btn-warning" data-toggle="modal"
                                data-target="#driverInfoModal">Fill out the form again
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-block btn-warning" data-toggle="modal"
                                data-target="#driverInfoModal">Edit driver info
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
