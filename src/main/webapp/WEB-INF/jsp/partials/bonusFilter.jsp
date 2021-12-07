<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.bonusFilter" var="loc"/>
<fmt:message bundle="${loc}" key="label.filter" var="filterLabel"/>
<fmt:message bundle="${loc}" key="label.firstName" var="firstNameLabel"/>
<fmt:message bundle="${loc}" key="label.lastName" var="lastNameLabel"/>
<fmt:message bundle="${loc}" key="label.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.discount" var="discountLabel"/>
<fmt:message bundle="${loc}" key="label.expireDate" var="expireDateLabel"/>

<div id="filter" class="col-md-3 order-md-1 mb-4">
    <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">${filterLabel}</span>
        <span id="filterCount" class="badge badge-secondary badge-pill"></span>
    </h4>
    <ul id="filterInputs" class="list-group mb-3">
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${firstNameLabel}</h6>
                <input class="form-control" id="firstName" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${lastNameLabel}</h6>
                <input class="form-control" id="lastName" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${phoneLabel}</h6>
                <input class="form-control" id="phone" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${discountLabel}</h6>
                <input class="form-control" id="discount" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">${expireDateLabel}</h6>
                <input class="form-control" id="expireDate" type="text">
            </div>
        </li>
    </ul>
</div>
