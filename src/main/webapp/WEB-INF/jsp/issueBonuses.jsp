<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>Issue bonuses</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/bonusesControl.js?v=1.0" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="container-fluid mt-3">
    <div class="row p-3">
        <jsp:include page="partials/bonusFilter.jsp"/>
        <div class="col-md-8 order-md-2">
            <div class="container">
                <div class="row">
                    <h2 class="order-1 mr-2">User bonuses</h2>
                    <button type="button" id="newBonusButton" class="btn btn-primary order-2"
                            data-toggle="modal" data-target="#bonusModal">New bonus
                    </button>
                </div>
                <br>
            </div>
            <div id="table"></div>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
<jsp:include page="partials/approveActionModal.jsp"/>
<jsp:include page="partials/newBonusModal.jsp"/>
</body>
</html>
