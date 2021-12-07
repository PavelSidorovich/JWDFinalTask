<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:requestEncoding value="utf-8"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.userControl" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitleLabel"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>

<html>
<head>
    <title>${pageTitleLabel}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/userControl.js?v=1.0" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="container-fluid mt-3">
    <div class="row p-3">
        <jsp:include page="partials/userFilter.jsp"/>
        <div class="col-md-8 order-md-2">
            <h2>${headerLabel}</h2>
            <br>
            <div id="table"></div>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
