<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Successful registration</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/cover/">
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="text-center">
<div class="jumbotron text-center mb-0">
    <h1 class="display-3">Thank You!</h1>
    <p class="lead">
        <strong>Please check your email</strong> for further instructions on how to complete your account setup.
    </p>
    <hr>
    <p class="lead">
        <a class="btn btn-primary btn-sm" href="${contextPath}/controller?command=main" role="button">
            Continue to homepage
        </a>
    </p>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
