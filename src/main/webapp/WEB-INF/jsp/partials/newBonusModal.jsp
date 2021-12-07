<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.newBonusModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.modal.title" var="modalTitleLabel"/>
<fmt:message bundle="${loc}" key="label.placeholder.discount" var="discountLabel"/>
<fmt:message bundle="${loc}" key="label.dateOfExpiration" var="expirationDateLabel"/>
<fmt:message bundle="${loc}" key="label.button.deselectAll" var="deselectButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.selectAll" var="selectButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>

<div id="bonusModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${modalTitleLabel}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="bonusForm" action="${contextPath}/controller?command=issue_bonus">
                    <div class="input-group mb-2 mr-sm-2">
                        <input type="text" name="discount" class="form-control" id="discount"
                               aria-describedby="discountFeedback" placeholder="${discountLabel}">
                        <div class="input-group-append">
                            <span class="input-group-text">%</span>
                        </div>
                        <div id="discountFeedback" class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="expireDate" class="col-form-label">${expirationDateLabel}</label>
                        <input type="date" name="expireDate" aria-describedby="expireDateFeedback" class="form-control"
                               id="expireDate"/>
                        <div id="expireDateFeedback" class="invalid-feedback"></div>
                    </div>
                    <div class="container-fluid">
                        <div class="row">
                            <button type="button" id="noneUsers" class="btn btn-outline-warning col mr-1">
                                ${deselectButtonLabel}
                            </button>
                            <button type="button" id="allUsers" aria-describedby="usersFeedback"
                                    class="btn btn-outline-success col ml-1">
                                ${selectButtonLabel}
                            </button>
                            <div id="usersFeedback" class="invalid-feedback"></div>
                        </div>
                    </div>
                    <br>
                    <div id="users"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">${cancelButtonLabel}</button>
                <button type="submit" form="bonusForm" class="btn btn-primary">${submitButtonLabel}</button>
            </div>
        </div>
    </div>
</div>