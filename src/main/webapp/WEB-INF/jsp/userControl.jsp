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
</head>

<body>
<jsp:include page="commonNav.jsp"/>
<table class="table table-bordered">
    <thead>
    <tr>
        <th>Role</th>
        <th>First name</th>
        <th>Last name</th>
        <th>Phone</th>
        <th>Email</th>
        <th>Money</th>
    </tr>
    </thead>
    <tbody id="myTable">
    <tr>
        <td>John</td>
        <td>Doe</td>
        <td>john@example.com</td>
    </tr>
    <c:forEach var="user" items="${requestScope.users}">
        <tr>
            <td>${user.account.role}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.account.phone}</td>
            <td>${user.email.get()}</td>
            <td>${user.money}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="commonFooter.jsp"/>
</body>
</html>
