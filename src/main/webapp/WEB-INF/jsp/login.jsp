<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">
    <title>Signin Template for Bootstrap</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <!-- Bootstrap core CSS -->
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="${contextPath}/css/signin.css" rel="stylesheet">
</head>

<body class="text-center">
<form class="form-signin" action="${contextPath}/controller?command=login" method="post">
    <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
    <label for="inputPhone" class="sr-only">Phone</label>
    <input type="tel" name="phone" id="inputPhone" class="form-control" placeholder="Phone number" required autofocus>
    <br>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
    <br>
    <c:if test="${not empty requestScope.errorLoginPassMessage}">
        <b>${requestScope.errorLoginPassMessage}</b>
        <br>
    </c:if>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    <br>
    <p>Don't have an account?</p>
    <a class="btn btn-outline-dark btn-primary btn-block" href="${contextPath}/controller?command=show_user_register">Sign up</a>
    <p class="mt-5 mb-3 text-muted">&copy; 2021-2021</p>
</form>
</body>
</html>
