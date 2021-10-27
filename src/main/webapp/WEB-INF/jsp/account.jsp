<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>Cool bikes</h3>
<table>
    <tr>
        <th>ID</th>
        <th>Phone</th>
        <th>Password hash</th>
        <%--        <th>Owner</th>--%>
    </tr>
    <c:forEach var="account" items="${requestScope.accounts}">
        <tr>
            <td>${account.id}</td>
            <td>${account.phone}</td>
            <td>${account.passwordHash}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
