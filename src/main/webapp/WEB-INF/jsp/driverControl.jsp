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
    <script src="${contextPath}/js/driverApplications.js?v=1.2" type="text/javascript"></script>
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
</div>
<jsp:include page="partials/applicationModal.jsp"/>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
