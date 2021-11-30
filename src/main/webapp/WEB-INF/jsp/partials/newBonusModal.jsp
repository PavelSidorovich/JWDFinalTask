<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div id="bonusModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">New user bonus</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="bonusForm" action="${contextPath}/controller?command=issue_bonus">
                    <div class="input-group mb-2 mr-sm-2">
                        <input type="text" name="discount" class="form-control" id="discount"
                               aria-describedby="discountFeedback" placeholder="Discount">
                        <div class="input-group-append">
                            <span class="input-group-text">%</span>
                        </div>
                        <div id="discountFeedback" class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="expireDate" class="col-form-label">Date of expiration:</label>
                        <input type="date" name="expireDate" aria-describedby="expireDateFeedback" class="form-control"
                               id="expireDate"/>
                        <div id="expireDateFeedback" class="invalid-feedback"></div>
                    </div>
                    <div class="container-fluid">
                        <div class="row">
                            <button type="button" id="noneUsers" class="btn btn-outline-warning col mr-1">deselect all
                                users
                            </button>
                            <button type="button" id="allUsers" aria-describedby="usersFeedback"
                                    class="btn btn-outline-success col ml-1">select all
                                users
                            </button>
                            <div id="usersFeedback" class="invalid-feedback"></div>
                        </div>
                    </div>
                    <br>
                    <div id="users"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" form="bonusForm" class="btn btn-primary">Confirm</button>
            </div>
        </div>
    </div>
</div>