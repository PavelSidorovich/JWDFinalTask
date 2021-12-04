<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.OrderStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>Make order</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <link href="${contextPath}/css/bootstrap.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/bootstrap.bundle.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/makeOrder.js?v=1.1" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator.min.js?v=1.0" type="text/javascript"></script>
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
                                <h2>Waiting for driver response</h2>
                                <div class="spinner-grow text-warning ml-2" role="status"></div>
                                <jsp:include page="partials/infoModal.jsp"/>
                                <script>showInfoModal()</script>
                            </c:when>
                            <c:otherwise>
                                <h2>Trip in progress</h2>
                                <div class="spinner-grow text-success ml-2" role="status"></div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <h2>Make order</h2>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${requestScope.taxis.size() eq 0}">
                <div class="alert alert-warning alert-dismissible fade show">
                    <strong>Sorry, there are no free drivers now. Please wait a few minutes :(</strong>
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
                <h4>From:</h4>
                <div class="form-row">
                    <div class="col-md-6 mb-3">
                        <input id="phone" type="text" name="phone" hidden value="${sessionScope.user.phone}">
                        <label for="longitudeFrom">Longitude</label>
                        <c:choose>
                            <c:when test="${not empty sessionScope.longitude}">
                                <input type="text" class="form-control" name="initialLongitude"
                                       id="longitudeFrom" value="${sessionScope.longitude}" readonly>
                            </c:when>
                            <c:otherwise>
                                <input type="text" class="form-control is-invalid"
                                       id="longitudeFrom" readonly>
                                <div class="invalid-feedback">Cannot define your current longitude</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="latitudeFrom">Latitude</label>
                        <c:choose>
                            <c:when test="${not empty sessionScope.latitude}">
                                <input type="text" class="form-control" name="initialLatitude"
                                       id="latitudeFrom" value="${sessionScope.latitude}" readonly>
                            </c:when>
                            <c:otherwise>
                                <input type="text" class="form-control is-invalid"
                                       id="latitudeFrom" readonly>
                                <div class="invalid-feedback">Cannot define your current latitude</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <h4>To:</h4>
                <div class="form-row">
                    <div class="col-md-6 mb-3">
                        <label for="longitudeTo">Longitude</label>
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
                        <label for="latitudeTo">Latitude</label>
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
                        <label for="taxis">Taxi</label>
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
                                    <option selected value="">Choose...</option>
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
                            <label for="bonuses">Bonus</label>
                            <select class="custom-select" id="bonuses" name="bonus">
                                <option selected value="">None</option>
                                <c:forEach var="bonus" items="${requestScope.bonuses}">
                                    <option value="${bonus.discount}">
                                            ${bonus.discount}% discount
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
                                <h5 id="price">Price: ${requestScope.order.price} RUB</h5>
                            </c:when>
                            <c:otherwise>
                                <h5 id="price"></h5>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${empty requestScope.order}">
                        <button class="btn btn-warning btn-block" type="submit" data-toggle="modal"
                                data-target="#infoModal">Call taxi
                        </button>
                    </c:if>
                </div>
            </form>
            <c:if test="${not empty requestScope.order}">
                <c:choose>
                    <c:when test="${requestScope.order.status eq OrderStatus.NEW}">
                        <jsp:include page="partials/approveActionModal.jsp"/>
                        <button id="cancelButton" class="btn btn-danger btn-block" type="submit">Cancel order</button>
                    </c:when>
                    <c:otherwise>
                        <button id="cancelButton" class="btn btn-danger btn-block disabled" type="submit">Cancel order
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
