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
<fmt:message bundle="${loc}" key="label.link.language.ch" var="zhLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.logout" var="logoutButtonLabel"/>
<link href="${contextPath}/css/menu.css?v=1.2" rel="stylesheet">
<script src="${contextPath}/js/languageSelector.js?v=1.1"></script>

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
            <button id="ru" class="dropdown-item">${ruLanguageLabel}</button>
            <button id="en" class="dropdown-item">${enLanguageLabel}</button>
            <button id="zh" class="dropdown-item">${zhLanguageLabel}</button>
        </div>
    </div>
    <c:if test="${not empty sessionScope.user}">
        <a class="btn btn-outline-danger" href="${contextPath}/controller?command=logout">
                ${logoutButtonLabel}
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                 class="bi bi-box-arrow-right" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                      d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2z"></path>
                <path fill-rule="evenodd"
                      d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"></path>
            </svg>
        </a>
    </c:if>
</nav>