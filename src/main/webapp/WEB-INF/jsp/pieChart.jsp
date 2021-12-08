<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%--<fmt:setLocale value="${cookie.lang.value}"/>--%>
<%--<fmt:setBundle basename="l10n.page.accountControl" var="loc"/>--%>
<%--<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>--%>
<%--<fmt:message bundle="${loc}" key="label.link.editPersonalInfo" var="editPersonalInfoLink"/>--%>
<%--<fmt:message bundle="${loc}" key="label.link.changePassword" var="changePasswordLink"/>--%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <%--    <title>${pageTitle}</title>--%>
    <title>Statistics</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <link href="${contextPath}/css/dashboard.css?v=1.1" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/pieChart.js?v=1.0"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="container-fluid">
    <div class="row">
        <c:import url="partials/adminSideBar.jsp"/>
        <main role="main" class="col-md-9 ml-sm-auto col-lg-9 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2">Ratio of order statuses</h1>
            </div>
            <canvas class="my-4 w-100" id="pieChart" width="900" height="380"></canvas>
        </main>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>

