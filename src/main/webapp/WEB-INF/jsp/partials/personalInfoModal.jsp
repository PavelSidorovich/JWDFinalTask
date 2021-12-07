<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.personalInfoModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.modal.title" var="modalTitleLabel"/>
<fmt:message bundle="${loc}" key="label.firstName" var="firstNameLabel"/>
<fmt:message bundle="${loc}" key="label.lastName" var="lastNameLabel"/>
<fmt:message bundle="${loc}" key="label.phone" var="phoneLabel"/>
<fmt:message bundle="${loc}" key="label.email" var="emailLabel"/>
<fmt:message bundle="${loc}" key="label.email.optional" var="emailOptionalLabel"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>

<div id="personalInfoModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${modalTitleLabel}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editPersonalInfoForm" action="${contextPath}/controller?command=edit_account_info"
                      method="post">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="firstName">${firstNameLabel}</label>
                            <input type="text" name="firstName" class="form-control is-valid" id="firstName"
                                   aria-describedby="firstNameFeedback" value="${requestScope.user.firstName}">
                            <div id="firstNameFeedback" class="invalid-feedback"></div>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="lastName">${lastNameLabel}</label>
                            <input type="text" name="lastName" class="form-control is-valid" id="lastName"
                                   aria-describedby="lastNameFeedback" value="${requestScope.user.lastName}">
                            <div id="lastNameFeedback" class="invalid-feedback"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="phone">${phoneLabel}</label>
                        <input type="tel" class="form-control" id="phone" readonly value="${requestScope.user.account.phone}">
                    </div>
                    <div class="form-group">
                        <label for="email">${emailLabel}<span class="text-muted">${emailOptionalLabel}</span></label>
                        <input type="email" name="email" class="form-control is-valid" id="email" aria-describedby="emailFeedback"
                               placeholder="example@gmail.com" value="${requestScope.user.email.orElse("")}">
                        <div id="emailFeedback" class="invalid-feedback"></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">${cancelButtonLabel}</button>
                <button id="submitButton" type="submit" form="editPersonalInfoForm" class="btn btn-primary">
                    ${submitButtonLabel}
                </button>
            </div>
        </div>
    </div>
</div>
