<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.OrderStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.userOrder" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header.pending" var="pendingHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.header.inProcess" var="inProcessLabel"/>
<fmt:message bundle="${loc}" key="label.header.makeOrder" var="makeOrderLabel"/>
<fmt:message bundle="${loc}" key="label.alert.warn" var="warnAlertLabel"/>
<fmt:message bundle="${loc}" key="label.header.from" var="fromLabel"/>
<fmt:message bundle="${loc}" key="label.coordinate.longitude" var="longitudeLabel"/>
<fmt:message bundle="${loc}" key="label.error.coordinate.longitude" var="longitudeErrorLabel"/>
<fmt:message bundle="${loc}" key="label.coordinate.latitude" var="latitudeLabel"/>
<fmt:message bundle="${loc}" key="label.error.coordinate.latitude" var="latitudeErrorLabel"/>
<fmt:message bundle="${loc}" key="label.header.to" var="toLabel"/>
<fmt:message bundle="${loc}" key="label.header.taxi" var="taxiHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.taxi.alt" var="taxiAltLabel"/>
<fmt:message bundle="${loc}" key="label.option.taxi" var="optionTaxiLabel"/>
<fmt:message bundle="${loc}" key="label.clientInfo.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.header.bonus" var="bonusLabel"/>
<fmt:message bundle="${loc}" key="label.bonus.none" var="bonusNoneLabel"/>
<fmt:message bundle="${loc}" key="label.bonus.discount" var="discountLabel"/>
<fmt:message bundle="${loc}" key="label.price" var="priceLabel"/>
<fmt:message bundle="${loc}" key="label.currency" var="currencyLabel"/>
<fmt:message bundle="${loc}" key="label.button.callTaxi" var="callTaxiButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <link href="${contextPath}/css/bootstrap.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.bundle.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/makeOrder.js?v=1.2" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/userNavBar.jsp"/>
<div class="container-fluid mt-3">
    <div class="row p-3">
        <div class="col-md-7 order-md-1 mb-4">
            <img class="img-fluid" src="../../images/map.png">
        </div>
        <div class="col-md-5 order-md-2">
            <div class="row">
                <c:choose>
                    <c:when test="${not empty requestScope.order}">
                        <c:choose>
                            <c:when test="${requestScope.order.status eq OrderStatus.NEW}">
                                <h2>${pendingHeaderLabel}</h2>
                                <div class="spinner-grow text-warning ml-2" role="status"></div>
                                <jsp:include page="partials/infoModal.jsp"/>
                                <script>showInfoModal()</script>
                            </c:when>
                            <c:otherwise>
                                <h2>${inProcessLabel}</h2>
                                <div class="spinner-grow text-success ml-2" role="status"></div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <h2>${makeOrderLabel}</h2>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${requestScope.taxis.size() eq 0}">
                <div class="alert alert-warning alert-dismissible fade show">
                    <strong>${warnAlertLabel}</strong>
                </div>
            </c:if>
            <c:if test="${not empty requestScope.funds}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <strong>${requestScope.funds}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <form id="orderForm" class="needs-validation" action="${contextPath}/controller?command=call_taxi"
                  novalidate method="post">
                <h4>${fromLabel}</h4>
                <div class="form-row">
                    <div class="col-md-6 mb-3">
                        <input id="phone" type="text" name="phone" hidden value="${sessionScope.user.phone}">
                        <label for="longitudeFrom">${longitudeLabel}</label>
                        <c:choose>
                            <c:when test="${not empty sessionScope.longitude}">
                                <input type="text" class="form-control" name="initialLongitude"
                                       id="longitudeFrom" value="${sessionScope.longitude}" readonly>
                            </c:when>
                            <c:otherwise>
                                <input type="text" class="form-control is-invalid"
                                       id="longitudeFrom" readonly>
                                <div class="invalid-feedback">${longitudeErrorLabel}</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="latitudeFrom">${latitudeLabel}</label>
                        <c:choose>
                            <c:when test="${not empty sessionScope.latitude}">
                                <input type="text" class="form-control" name="initialLatitude"
                                       id="latitudeFrom" value="${sessionScope.latitude}" readonly>
                            </c:when>
                            <c:otherwise>
                                <input type="text" class="form-control is-invalid"
                                       id="latitudeFrom" readonly>
                                <div class="invalid-feedback">${latitudeErrorLabel}</div>
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
                                <c:choose>
                                    <c:when test="${not empty requestScope.endLongitude}">
                                        <input type="text" class="form-control" name="endLongitude" id="longitudeTo"
                                               value="${requestScope.endLongitude}">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" class="form-control" name="endLongitude" id="longitudeTo">
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${not empty requestScope.endLongitudeInvalid}">
                            <div id="endLongitudeFeedback"
                                 class="invalid-feedback">${requestScope.endLongitudeInvalid}</div>
                        </c:if>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="latitudeTo">${latitudeLabel}</label>
                        <c:choose>
                            <c:when test="${not empty requestScope.order}">
                                <input type="text" class="form-control" name="endLatitude" id="latitudeTo"
                                       value="${requestScope.order.endCoordinates.latitude}" readonly>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty requestScope.endLatitude}">
                                        <input type="text" class="form-control" name="endLatitude" id="latitudeTo"
                                               value="${requestScope.endLatitude}">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" class="form-control" name="endLatitude" id="latitudeTo">
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${not empty requestScope.endLatitudeInvalid}">
                            <div id="endLatitudeFeedback"
                                 class="invalid-feedback">${requestScope.endLatitudeInvalid}</div>
                        </c:if>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="taxis">${taxiHeaderLabel}</label>
                        <c:choose>
                            <c:when test="${not empty requestScope.order}">
                                <select class="custom-select" id="taxis" name="taxi"
                                        aria-describedby="validationTaxiFeedback" disabled>
                                    <option>
                                            ${requestScope.order.driver.taxi.carBrand}
                                            ${requestScope.order.driver.taxi.carModel}
                                        (${requestScope.order.driver.taxi.licencePlate})
                                    </option>
                                </select>
                                <img src="../images/taxes/${order.driver.taxi.photoFilepath}" alt="taxi photo"
                                     class="img-fluid">
                            </c:when>
                            <c:otherwise>
                                <select class="custom-select" id="taxis" name="taxi"
                                        aria-describedby="validationTaxiFeedback">
                                    <option selected value="">${optionTaxiLabel}</option>
                                    <c:forEach var="taxi" items="${requestScope.taxis}">
                                        <option value="${taxi.licencePlate}">
                                                ${taxi.carBrand} ${taxi.carModel} (${taxi.licencePlate})
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty requestScope.taxi}">
                                    <div id="validationTaxiFeedback" class="invalid-feedback"></div>
                                    <div id="endLatitudeFeedback"
                                         class="invalid-feedback">${requestScope.taxi}</div>
                                </c:if>
                                <img id="taxiPreview" src="" alt="taxi photo" class="img-fluid">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${empty requestScope.order}">
                        <div class="col-md-6 mb-3">
                            <label for="bonuses">${bonusLabel}</label>
                            <select class="custom-select" id="bonuses" name="bonus">
                                <option selected value="">${bonusNoneLabel}</option>
                                <c:forEach var="bonus" items="${requestScope.bonuses}">
                                    <option value="${bonus.discount}">
                                            ${bonus.discount}${discountLabel}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:if>
                </div>
                <div class="container-fluid p-0 mt-4 text-right">
                    <div class="container-fluid mb-4">
                        <c:choose>
                            <c:when test="${not empty requestScope.order}">
                                <h5 id="price">${priceLabel} ${requestScope.order.price} ${currencyLabel}</h5>
                            </c:when>
                            <c:otherwise>
                                <h5 id="price"></h5>
                                <script>addPriceParametersListener();</script>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${empty requestScope.order}">
                        <button class="btn btn-warning btn-block" type="submit" data-toggle="modal"
                                data-target="#infoModal">
                            ${callTaxiButtonLabel}
                        </button>
                    </c:if>
                </div>
            </form>
            <c:if test="${not empty requestScope.order}">
                <c:choose>
                    <c:when test="${requestScope.order.status eq OrderStatus.NEW}">
                        <jsp:include page="partials/approveActionModal.jsp"/>
                        <button id="cancelButton" class="btn btn-danger btn-block" type="submit">${cancelButtonLabel}</button>
                    </c:when>
                    <c:otherwise>
                        <button id="cancelButton" class="btn btn-danger btn-block disabled" type="submit">
                                ${cancelButtonLabel}
                        </button>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
