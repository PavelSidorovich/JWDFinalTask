<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.approveActionModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.confirm" var="confirmButtonLabel"/>

<div id="modalApprove" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div id="modalHeader" class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p id="modalMessage"></p>
            </div>
            <div id="modalButtons" class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">${cancelButtonLabel}</button>
                <button type="button" id="approveButton" class="btn btn-primary">${confirmButtonLabel}</button>
            </div>
        </div>
    </div>
</div>
