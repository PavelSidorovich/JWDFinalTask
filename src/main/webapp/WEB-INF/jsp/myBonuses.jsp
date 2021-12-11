<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwds" uri="com.sidorovich.pavel" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.myBonuses" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.prompt" var="promptLabel"/>
<fmt:message bundle="${loc}" key="label.placeholder" var="placeholderLabel"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/myBonuses.js?v=1.0"></script>
</head>

<body>
<jsp:include page="partials/userNavBar.jsp"/>
<div class="album py-5 bg-white">
    <div class="container">
        <h2>${headerLabel}<span id="filterCount" class="badge badge-secondary badge-pill"></span></h2>
        <p>${promptLabel}</p>
        <input class="form-control" id="searchInput" type="text" placeholder="${placeholderLabel}">
        <br>
        <div id="bonusCardContainer" class="row row-cols-1 row-cols-md-3 text-center">
            <c:if test="${not empty requestScope.userBonuses}">
                <c:forEach var="bonus" items="${requestScope.userBonuses}">
                    <div class="col mb-4">
                        <jwds:bonusDisplayer bonus="${bonus}" localizationContext="${loc}"/>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>
    <jsp:include page="partials/approveActionModal.jsp"/>
    <jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
