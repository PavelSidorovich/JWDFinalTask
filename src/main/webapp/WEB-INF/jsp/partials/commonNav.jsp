<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.commonNav" var="loc"/>
<fmt:message bundle="${loc}" key="label.company" var="companyLabel"/>
<fmt:message bundle="${loc}" key="label.link.becomeDriver" var="becomeDriverLink"/>
<fmt:message bundle="${loc}" key="label.link.logout" var="logoutLink"/>
<fmt:message bundle="${loc}" key="label.link.login" var="loginLink"/>
<fmt:message bundle="${loc}" key="label.button.language" var="languageLabel"/>
<fmt:message bundle="${loc}" key="label.link.language.ru" var="ruLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.language.en" var="enLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.language.ch" var="zhLanguageLabel"/>
<fmt:message bundle="${loc}" key="label.link.logout" var="logoutButtonLabel"/>
<script src="${contextPath}/js/languageSelector.js?v=1.1"></script>

<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow">
    <h5 class="my-0 mr-md-auto font-weight-normal">${companyLabel}</h5>
    <nav class="my-2 my-md-0 mr-md-3">
        <c:if test="${empty sessionScope.user}">
            <a class="p-2 text-dark" href="${contextPath}/controller?command=show_driver_register">${becomeDriverLink}</a>
        </c:if>
    </nav>
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
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <a class="btn btn-outline-primary" href="${contextPath}/controller?command=logout">${logoutLink}</a>
        </c:when>
        <c:otherwise>
            <a class="btn btn-outline-primary" href="${contextPath}/controller?command=show_login">${loginLink}</a>
        </c:otherwise>
    </c:choose>
</div>
