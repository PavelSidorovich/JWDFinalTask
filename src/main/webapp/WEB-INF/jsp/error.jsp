<%@ page isErrorPage="true" contentType="text/html"%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">
    <title>Error</title>
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">
    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/error.css" rel="stylesheet">
</head>

<div class="d-flex justify-content-center align-items-center" id="main">
    <h1 class="mr-3 pr-3 align-top border-right inline-block align-content-center">${pageContext.errorData.statusCode}</h1>
    <div class="inline-block align-middle">
        <h2 class="font-weight-normal lead" id="desc">Something went wrong...</h2>
    </div>
</div>
