<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.infoModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.modal.title" var="modalTitleLabel"/>
<fmt:message bundle="${loc}" key="label.modal.body" var="modalBodyLabel"/>
<fmt:message bundle="${loc}" key="label.button.ok" var="okButtonLabel"/>

<div id="infoModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${modalTitleLabel}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>${modalBodyLabel}</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">${okButtonLabel}</button>
            </div>
        </div>
    </div>
</div>