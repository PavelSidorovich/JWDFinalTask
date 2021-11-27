<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/userControl.js"></script>
</head>

<body>
<jsp:include page="commonNav.jsp"/>
<div class="container mt-3">
    <div class="row">
        <div id="filter" class="col-md-4 order-md-1 mb-4">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
                <span class="text-muted">Filter</span>
                <span id="filterCount" class="badge badge-secondary badge-pill"></span>
            </h4>
            <ul class="list-group mb-3">
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0">Role</h6>
                        <input class="form-control" id="role" type="text">
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0">First name</h6>
                        <input class="form-control" id="firstName" type="text">
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0">Last name</h6>
                        <input class="form-control" id="lastName" type="text">
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0">Phone</h6>
                        <input class="form-control" id="phone" type="text">
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0">Email</h6>
                        <input class="form-control" id="email" type="text">
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0">Cash</h6>
                        <input class="form-control" id="cash" type="text">
                    </div>
                </li>
            </ul>
        </div>
        <div class="col-md-8 order-md-2">
            <h2>Buber users</h2>
            <br>
            <table id="table" class="table table-bordered">
                <thead>
                <tr>
                    <th>Role</th>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Cash</th>
                    <th>Status managing</th>
                </tr>
                </thead>
                <tbody id="userTable"></tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="commonFooter.jsp"/>
</body>
</html>
