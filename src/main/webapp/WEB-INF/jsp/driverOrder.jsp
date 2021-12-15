<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.OrderStatus" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.DriverStatus" %>
<jsp:useBean id="driverStatus" scope="request" type="com.sidorovich.pavel.buber.api.model.DriverStatus"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.driverOrder" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.alt.cityMap" var="cityMapLabel"/>
<fmt:message bundle="${loc}" key="label.alert.danger" var="dangerAlertLabel"/>
<fmt:message bundle="${loc}" key="label.header.pending" var="pendingHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.header.inProcess" var="processHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.header.rest" var="restHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.header.free" var="freeHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.checkbox" var="checkboxLabel"/>
<fmt:message bundle="${loc}" key="label.header.from" var="fromLabel"/>
<fmt:message bundle="${loc}" key="label.coordinate.longitude" var="longitudeLabel"/>
<fmt:message bundle="${loc}" key="label.coordinate.latitude" var="latitudeLabel"/>
<fmt:message bundle="${loc}" key="label.header.to" var="toLabel"/>
<fmt:message bundle="${loc}" key="label.header.clientInfo" var="clientInfoLabel"/>
<fmt:message bundle="${loc}" key="label.clientInfo.firstName" var="firstNameLabel"/>
<fmt:message bundle="${loc}" key="label.clientInfo.lastName" var="lastNameLabel"/>
<fmt:message bundle="${loc}" key="label.clientInfo.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.clientInfo.email" var="emailLabel"/>
<fmt:message bundle="${loc}" key="label.header.price" var="priceLabel"/>
<fmt:message bundle="${loc}" key="label.rubles" var="rublesLabel"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.takeOrder" var="takeOrderButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.confirm" var="confirmButtonLabel"/>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/driverOrder.js?v=1.5" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/driverNavBar.jsp"/>
<div class="container-fluid mt-3">
    <div class="row p-3">
        <div class="col-md-7 order-md-1 mb-4">
            <img class="img-fluid" src="${contextPath}/images/map.png" alt="${cityMapLabel}">
        </div>
        <div class="col-md-5 order-md-2">
            <c:choose>
                <c:when test="${driverStatus eq DriverStatus.PENDING or driverStatus eq DriverStatus.REJECTED}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <strong>${dangerAlertLabel}</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty requestScope.order}">
                                <c:choose>
                                    <c:when test="${requestScope.order.status eq OrderStatus.NEW}">
                                        <h2>${pendingHeaderLabel}</h2>
                                        <div id="statusSpinner" class="spinner-grow text-danger ml-2"
                                             role="status"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <h2>${processHeaderLabel}</h2>
                                        <div id="statusSpinner" class="spinner-grow text-success ml-2"
                                             role="status"></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${not empty driverStatus}">
                                    <c:choose>
                                        <c:when test="${driverStatus eq DriverStatus.REST}">
                                            <h2 id="statusLine">${restHeaderLabel}</h2>
                                        </c:when>
                                        <c:otherwise>
                                            <h2 id="statusLine">${freeHeaderLabel}</h2>
                                            <div id="statusSpinner" class="spinner-grow text-warning ml-2"
                                                 role="status"></div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <div class="container mt-2 mb-2">
                                    <div class="custom-control custom-switch">
                                        <input type="checkbox" class="custom-control-input" id="statusCheckbox">
                                        <label class="custom-control-label"
                                               for="statusCheckbox">${checkboxLabel}</label>
                                    </div>
                                    <script>getDriverStatus()</script>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty sessionScope.user}">
                <input hidden id="driverId" value="${sessionScope.user.getId().get()}">
            </c:if>
            <h4>${fromLabel}</h4>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="longitudeFrom">${longitudeLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" name="initialLongitude"
                                   id="longitudeFrom" value="${requestScope.order.initialCoordinates.longitude}"
                                   readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control"
                                   id="longitudeFrom" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="latitudeFrom">${latitudeLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" name="initialLatitude"
                                   id="latitudeFrom" value="${requestScope.order.initialCoordinates.latitude}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control"
                                   id="latitudeFrom" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <h4>${toLabel}</h4>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="longitudeTo">${longitudeLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" name="endLongitude" id="longitudeTo"
                                   value="${requestScope.order.endCoordinates.longitude}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" name="endLongitude" id="longitudeTo" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="latitudeTo">${latitudeLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" name="endLatitude" id="latitudeTo"
                                   value="${requestScope.order.endCoordinates.latitude}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" name="endLatitude" id="latitudeTo" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <h4>${clientInfoLabel}</h4>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="firstName">${firstNameLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" id="firstName"
                                   value="${requestScope.order.client.firstName}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="firstName" value="" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="lastName">${lastNameLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" id="lastName"
                                   value="${requestScope.order.client.lastName}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="lastName" value="" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="phone">${phoneLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" id="phone"
                                   value="${requestScope.order.client.account.phone}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="phone" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="email">${emailLabel}</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" id="email"
                                   value="${requestScope.order.client.email.get()}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="email" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="container-fluid p-0 mt-4 text-right">
                <div class="container-fluid mb-4">
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <h5 id="price">${priceLabel} ${requestScope.order.price} ${rublesLabel}</h5>
                        </c:when>
                        <c:otherwise>
                            <h5 id="price"></h5>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <c:if test="${not empty requestScope.order}">
                <jsp:include page="partials/approveActionModal.jsp"/>
                <c:choose>
                    <c:when test="${requestScope.order.status eq OrderStatus.NEW and driverStatus
                     ne DriverStatus.REJECTED and driverStatus ne DriverStatus.PENDING }">
                        <div class="form-row">
                            <div class="col-md-6 mb-3">
                                <button id="cancelButton" class="form-control btn btn-outline-danger btn-block">
                                        ${cancelButtonLabel}
                                </button>
                            </div>
                            <div class="col-md-6 mb-3">
                                <button id="takeButton" class="form-control btn btn-success">
                                        ${takeOrderButtonLabel}
                                </button>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <button id="confirmOrderButton" class="btn btn-success btn-block">${confirmButtonLabel}</button>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
</div>
<script>
  const freeStatus = '${freeHeaderLabel}';
  const restStatus = '${restHeaderLabel}';
  const cancelButtonLabel = '${cancelButtonLabel}';
  const takeButtonLabel = '${takeOrderButtonLabel}';
  const confirmButtonLabel = '${confirmButtonLabel}';
  const approveCancelHeader = '<fmt:message bundle="${loc}" key="label.header.approveCancel"/>';
  const approveTakingHeader = '<fmt:message bundle="${loc}" key="label.header.approveTaking"/>';
  const confirmPaymentHeader = '<fmt:message bundle="${loc}" key="label.header.confirmPayment"/>';
  const approveCancelling = '<fmt:message bundle="${loc}" key="label.approveCancel"/>';
  const approveTaking = '<fmt:message bundle="${loc}" key="label.approveTaking"/>';
  const confirmPayment = '<fmt:message bundle="${loc}" key="label.confirmPayment"/>';
</script>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
