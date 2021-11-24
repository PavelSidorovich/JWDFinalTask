<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <%--    <link rel="icon" href="${contextPath}/docs/4.0/assets/img/favicons/favicon.ico">--%>
    <title>Signin Template for Bootstrap</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <!-- Bootstrap core CSS -->
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/register.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/validator.js"></script>
</head>

<body class="text-center">
<form id="registerForm" class="form-register" action="${contextPath}/controller?command=user_register">
    <img class="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72"
         height="72">
    <h1 class="h3 mb-3 font-weight-normal">Registration</h1>
    <label for="inputFName" class="sr-only">First name</label>
    <input type="text" name="fName" id="inputFName" class="form-control" placeholder="First name" required autofocus>
    <br>
    <label for="inputLName" class="sr-only">Last name</label>
    <input type="text" name="lName" id="inputLName" class="form-control" placeholder="Last name" required>
    <br>
    <label for="inputPhone" class="sr-only">Phone</label>
    <input type="tel" name="phone" id="inputPhone" class="form-control" placeholder="Phone number" required>
    <br>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
    <br>
    <label for="inputPasswordAgain" class="sr-only">Enter password again</label>
    <input type="password" name="passwordRepeat" id="inputPasswordAgain" class="form-control"
           placeholder="Enter password again" required>
    <br>
    <label for="inputEmail" class="sr-only">Email</label>
    <input type="email" name="email" id="inputEmail" class="form-control" placeholder="Email">
    <br>
    <b id="errorRegisterMsg"></b>
    <br><br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>
    <br>
    <p>Already have an account?</p>
    <a class="btn btn-outline-dark btn-primary btn-block" href="${contextPath}/controller?command=show_login">Sign
        in</a>
    <p class="mt-5 mb-3 text-muted">&copy; 2021-2021</p>
</form>
<script>
  $("#registerForm").submit(function (event) {
    event.preventDefault();

    let form = $(this),
      url = form.attr("action");
    let posting = $.post(url,
      {
        fName: form.find("input[name='fName']").val(),
        lName: form.find("input[name='lName']").val(),
        phone: form.find("input[name='phone']").val(),
        password: form.find("input[name='password']").val(),
        passwordRepeat: form.find("input[name='passwordRepeat']").val(),
        email: form.find("input[name='email']").val(),
      }
    );

    posting.done(function (data) {
      console.log(data.path);
      if (data.isRedirect === false) {
        $('#errorRegisterMsg').text(data.obj);
      } else {
        window.location.replace(data.path);
      }
      console.log(data);
      // if (!data.includes("html")) {
      //   $('#errorRegisterMsg').text(data);
      // } else {
      //   document.write(data);
      // }
    });
  });
</script>
</body>
</html>
