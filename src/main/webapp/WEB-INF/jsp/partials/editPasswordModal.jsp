<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.editPasswordModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.modal.title" var="modalTitleLabel"/>
<fmt:message bundle="${loc}" key="label.password" var="passwordLabel"/>
<fmt:message bundle="${loc}" key="label.repeatPassword" var="passwordRepeatLabel"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>

<div id="editPasswordModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${modalTitleLabel}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editPasswordForm" action="${contextPath}/controller?command=change_password" method="post">
                    <div class="form-group">
                        <label for="password">${passwordLabel}</label>
                        <input type="password" name="password" class="form-control is-invalid" id="password"
                               aria-describedby="passwordFeedback">
                        <div id="passwordFeedback" class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="passwordRepeat">${passwordRepeatLabel}</label>
                        <input type="password" name="passwordRepeat" class="form-control is-invalid" id="passwordRepeat"
                               aria-describedby="passwordRepeatFeedback">
                        <div id="passwordRepeatFeedback" class="invalid-feedback"></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">${cancelButtonLabel}</button>
                <button id="editPasswordButton" type="submit" form="editPasswordForm" class="btn btn-primary" disabled>
                    ${submitButtonLabel}
                </button>
            </div>
        </div>
    </div>
</div>
