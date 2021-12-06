<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div id="editPasswordModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Change password</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editPasswordForm" action="${contextPath}/controller?command=change_password" method="post">
                    <div class="form-group">
                        <label for="password">New password</label>
                        <input type="password" name="password" class="form-control" id="password"
                               aria-describedby="passwordFeedback">
                        <div id="passwordFeedback" class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="passwordRepeat">Repeat password</label>
                        <input type="password" name="passwordRepeat" class="form-control" id="passwordRepeat"
                               aria-describedby="passwordRepeatFeedback">
                        <div id="passwordRepeatFeedback" class="invalid-feedback"></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancel</button>
                <button id="editPasswordButton" type="submit" form="editPasswordForm" class="btn btn-primary">
                    Edit password
                </button>
            </div>
        </div>
    </div>
</div>
