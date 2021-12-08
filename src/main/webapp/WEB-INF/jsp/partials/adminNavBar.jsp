<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.adminNavBar" var="loc"/>
<fmt:message bundle="${loc}" key="label.company" var="companyLabel"/>
<fmt:message bundle="${loc}" key="label.link.manageUsers" var="manageUsersLink"/>
<fmt:message bundle="${loc}" key="label.link.manageDrivers" var="manageDriversLink"/>
<fmt:message bundle="${loc}" key="label.link.issueBonuses" var="issueBonusesLink"/>
<fmt:message bundle="${loc}" key="label.link.statistics" var="statisticsLink"/>
<fmt:message bundle="${loc}" key="label.link.logout" var="logoutLink"/>
<link href="${contextPath}/css/menu.css?v=1.1" rel="stylesheet">

<nav class="navbar sticky-top navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">${companyLabel}</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText"
            aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-md-center" id="navbarText">
        <ul id="menu" class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_user_control">${manageUsersLink}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=driver_applications">${manageDriversLink}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_bonuses">${issueBonusesLink}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_pie_chart">${statisticsLink}</a>
            </li>
        </ul>
    </div>
    <c:if test="${not empty sessionScope.user}">
        <a class="btn btn-outline-danger" href="${contextPath}/controller?command=logout">${logoutLink}</a>
    </c:if>
</nav>