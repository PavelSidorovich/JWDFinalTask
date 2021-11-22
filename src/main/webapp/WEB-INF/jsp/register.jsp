<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/register.css" rel="stylesheet">
</head>

<body class="text-center">
<form class="form-register">
    <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Registration</h1>
    <label for="inputFName" class="sr-only">First name</label>
    <input type="text" id="inputFName" class="form-control" placeholder="First name" required autofocus>
    <br>
    <label for="inputLName" class="sr-only">Last name</label>
    <input type="text" id="inputLName" class="form-control" placeholder="Last name" required>
    <br>
    <label for="inputPhone" class="sr-only">Phone</label>
    <input type="tel" id="inputPhone" class="form-control" placeholder="Phone number" required>
    <br>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
    <br>
    <label for="inputPasswordAgain" class="sr-only">Enter password again</label>
    <input type="password" id="inputPasswordAgain" class="form-control" placeholder="Enter password again" required>
    <br>
    <label for="inputEmail" class="sr-only">Email</label>
    <input type="password" id="inputEmail" class="form-control" placeholder="Email">
    <br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>
    <p class="mt-5 mb-3 text-muted">&copy; 2021-2021</p>
</form>
</body>
</html>
