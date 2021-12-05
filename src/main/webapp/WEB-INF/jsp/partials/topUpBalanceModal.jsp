<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div id="topUpBalanceModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Top up balance</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="topUpBalanceForm" action="${contextPath}/controller?command=top_up_balance" method="post">
                    <label class="sr-only" for="cash">Username</label>
                    <div class="input-group mb-2 mr-sm-2">
                        <input type="text" name="cash" class="form-control" id="cash"
                               aria-describedby="discountFeedback" placeholder="Money">
                        <div class="input-group-append">
                            <div class="input-group-text">RUB</div>
                        </div>
                    </div>
                    <div id="cashFeedback" class="invalid-feedback"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancel</button>
                <button id="submitButton" type="submit" form="topUpBalanceForm" class="btn btn-primary">Put on wallet</button>
            </div>
        </div>
    </div>
</div>
