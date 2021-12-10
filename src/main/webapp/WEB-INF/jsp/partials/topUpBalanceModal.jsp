<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.topUpBalanceModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.modal.title" var="modalTitleLabel"/>
<fmt:message bundle="${loc}" key="label.placeholder.money" var="moneyPlaceholderLabel"/>
<fmt:message bundle="${loc}" key="label.rubles" var="rublesLabel"/>
<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>

<div id="topUpBalanceModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${modalTitleLabel}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="topUpBalanceForm" action="${contextPath}/controller?command=top_up_balance" method="post">
                    <label class="sr-only" for="cash"></label>
                    <div class="input-group mb-2 mr-sm-2">
                        <input type="text" name="cash" class="form-control" id="cash"
                               aria-describedby="discountFeedback" placeholder="${moneyPlaceholderLabel}">
                        <div class="input-group-append">
                            <div class="input-group-text">${rublesLabel}</div>
                        </div>
                    </div>
                    <div id="cashFeedback" class="invalid-feedback"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary"
                        data-dismiss="modal">${cancelButtonLabel}</button>
                <button id="submitButton" type="submit" form="topUpBalanceForm"
                        class="btn btn-primary">${submitButtonLabel}</button>
            </div>
        </div>
    </div>
</div>

<script>
  $(document).ready(function () {
    addCashValidator("Valid cash value is required (for example, 500.00)");
  });
</script>