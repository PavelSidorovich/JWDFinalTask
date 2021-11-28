<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Title</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/driverApplications.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="album py-5 bg-white">
    <div class="container">
        <h2>Drivers applications<span id="filterCount" class="badge badge-secondary badge-pill"></span></h2>

        <p>Type something in the input field to search the cards for first names, last names, phones, cars, status or
            emails:</p>
        <input class="form-control" id="searchInput" type="text" placeholder="Search..">
        <br>
        <div id="cardContainer" class="row"></div>
    </div>

    <%-- modal window for viewing details--%>
    <div class="modal-body" style="display: block">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-4">.col-md-4</div>
                <div class="col-md-4 ml-auto">.col-md-4 .ml-auto</div>
            </div>
            <div class="row">
                <div class="col-md-3 ml-auto">.col-md-3 .ml-auto</div>
                <div class="col-md-2 ml-auto">.col-md-2 .ml-auto</div>
            </div>
            <div class="row">
                <div class="col-md-6 ml-auto">.col-md-6 .ml-auto</div>
            </div>
            <div class="row">
                <div class="col-sm-9">
                    Level 1: .col-sm-9
                    <div class="row">
                        <div class="col-8 col-sm-6">
                            Level 2: .col-8 .col-sm-6
                        </div>
                        <div class="col-4 col-sm-6">
                            Level 2: .col-4 .col-sm-6
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
