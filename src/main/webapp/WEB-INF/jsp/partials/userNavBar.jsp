<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.userNavBar" var="loc"/>
<fmt:message bundle="${loc}" key="label.company" var="companyLabel"/>
<fmt:message bundle="${loc}" key="label.link.makeOrder" var="makeOrderLabel"/>
<fmt:message bundle="${loc}" key="label.link.myBonuses" var="myBonusesLabel"/>
<fmt:message bundle="${loc}" key="label.link.accountControl" var="accountControlLabel"/>
<fmt:message bundle="${loc}" key="label.link.myWallet" var="myWalletLabel"/>
<fmt:message bundle="${loc}" key="label.button.language" var="languageLabel"/>
<fmt:message bundle="${loc}" key="label.link.language.ru" var="ruLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.language.en" var="enLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.language.ch" var="chLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.logout" var="logoutButtonLabel"/>
<link href="${contextPath}/css/menu.css?v=1.2" rel="stylesheet">

<nav class="navbar sticky-top navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">${companyLabel}</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText"
            aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-md-center" id="navbarText">
        <ul id="menu" class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=make_order">${makeOrderLabel}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=my_bonuses">${myBonusesLabel}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=account_control">${accountControlLabel}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=my_wallet">${myWalletLabel}</a>
            </li>
        </ul>
    </div>
    <div class="btn-group pr-5">
        <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-toggle="dropdown"
                aria-expanded="false">
            ${languageLabel}
        </button>
        <div class="dropdown-menu">
            <form action="${contextPath}/controller?command=change_language" method="post">
                <button class="dropdown-item">${ruLanguageLabel}</button>
            </form>

            <a class="dropdown-item" href="#">${enLanguageLabel}</a>
            <a class="dropdown-item" href="#">${chLanguageLabel}</a>
        </div>
    </div>
    <c:if test="${not empty sessionScope.user}">
        <a class="btn btn-outline-danger" href="${contextPath}/controller?command=logout">${logoutButtonLabel}</a>
    </c:if>
</nav>