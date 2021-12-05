<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.OrderStatus" %>
<%@ page import="com.sidorovich.pavel.buber.api.model.DriverStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>Incoming order</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <link href="${contextPath}/css/bootstrap.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/bootstrap.bundle.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/driverOrder.js?v=1.1" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/driverNavBar.jsp"/>
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
                                <h2>Waiting for your response</h2>
                                <div class="spinner-grow text-danger ml-2" role="status"></div>
                                <%--                                <jsp:include page="partials/infoModal.jsp"/>--%>
                                <%--                                <script>showInfoModal()</script>--%>
                            </c:when>
                            <c:otherwise>
                                <h2>Trip in progress</h2>
                                <div class="spinner-grow text-success ml-2" role="status"></div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${not empty requestScope.driverStatus}">
                            <c:choose>
                                <c:when test="${requestScope.driverStatus eq DriverStatus.BUSY}">
                                    <h2 id="statusLine">You are taking rest...</h2>
                                </c:when>
                                <c:otherwise>
                                    <h2 id="statusLine">Waiting for orders</h2>
                                    <div id="statusSpinner" class="spinner-grow text-warning ml-2" role="status"></div>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <div class="container mt-2 mb-2">
                            <div class="custom-control custom-switch">
                                <input type="checkbox" class="custom-control-input" id="statusCheckbox">
                                <label class="custom-control-label" for="statusCheckbox">Take orders</label>
                            </div>
                            <script>getDriverStatus()</script>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <%--            todo--%>
            <%--            <c:if test="${not empty requestScope.funds}">--%>
            <%--                <div class="alert alert-danger alert-dismissible fade show">--%>
            <%--                    <strong>${requestScope.funds}</strong>--%>
            <%--                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">--%>
            <%--                        <span aria-hidden="true">&times;</span>--%>
            <%--                    </button>--%>
            <%--                </div>--%>
            <%--            </c:if>--%>
            <c:if test="${not empty sessionScope.user}">
                <input hidden id="driverId" value="${sessionScope.user.getId().get()}">
            </c:if>
            <h4>From:</h4>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="longitudeFrom">Longitude</label>
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
                    <label for="latitudeFrom">Latitude</label>
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
                            <input type="text" class="form-control" name="endLongitude" id="longitudeTo" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="latitudeTo">Latitude</label>
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
            <h4>Client information:</h4>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="firstName">First name</label>
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
                    <label for="lastName">Last name</label>
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
                    <label for="phone">Phone</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" id="phone"
                                   value="${requestScope.order.client.account.phone}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="phone" value="" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="email">Email</label>
                    <c:choose>
                        <c:when test="${not empty requestScope.order}">
                            <input type="text" class="form-control" id="email"
                                   value="${requestScope.order.client.email.orElse('None')}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="email" value="" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
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
            </div>
            <c:if test="${not empty requestScope.order}">
                <jsp:include page="partials/approveActionModal.jsp"/>
                <c:choose>
                    <c:when test="${requestScope.order.status eq OrderStatus.NEW}">
                        <%--                        <jsp:include page="partials/approveActionModal.jsp"/>--%>
                        <div class="form-row">
                            <div class="col-md-6 mb-3">
                                <button id="cancelButton" class="form-control btn btn-outline-danger btn-block">
                                    Cancel order
                                </button>
                            </div>
                            <div class="col-md-6 mb-3">
                                <button id="takeButton" class="form-control btn btn-success">
                                    Take order
                                </button>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <button id="confirmOrderButton" class="btn btn-success btn-block">Confirm payment</button>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
