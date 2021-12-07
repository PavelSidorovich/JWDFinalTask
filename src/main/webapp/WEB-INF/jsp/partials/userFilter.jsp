<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.userFilter" var="loc"/>
<fmt:message bundle="${loc}" key="label.filter.title" var="filterTitleLabel"/>
<fmt:message bundle="${loc}" key="label.filter.role" var="filterRoleLabel"/>
<fmt:message bundle="${loc}" key="label.filter.firstName" var="filterFirstNameLabel"/>
<fmt:message bundle="${loc}" key="label.filter.lastName" var="filterLastNameLabel"/>
<fmt:message bundle="${loc}" key="label.filter.phone" var="filterPhoneLabel"/>
<fmt:message bundle="${loc}" key="label.filter.email" var="filterEmailLabel"/>
<fmt:message bundle="${loc}" key="label.filter.cash" var="filterCashLabel"/>

<div id="filter" class="col-md-3 order-md-1 mb-4">
    <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">${filterTitleLabel}</span>
        <span id="filterCount" class="badge badge-secondary badge-pill"></span>
    </h4>
    <ul id="filterInputs" class="list-group mb-3">
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${filterRoleLabel}</h6>
                <input class="form-control" id="role" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${filterFirstNameLabel}</h6>
                <input class="form-control" id="firstName" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${filterLastNameLabel}</h6>
                <input class="form-control" id="lastName" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${filterPhoneLabel}</h6>
                <input class="form-control" id="phone" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${filterEmailLabel}</h6>
                <input class="form-control" id="email" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${filterCashLabel}</h6>
                <input class="form-control" id="cash" type="text">
            </div>
        </li>
    </ul>
</div>
