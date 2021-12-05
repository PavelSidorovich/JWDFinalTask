<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div id="personalInfoModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Personal information</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editPersonalInfoForm" action="${contextPath}/controller?command=edit_account_info"
                      method="post">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="firstName">First name</label>
                            <input type="text" name="firstName" class="form-control is-valid" id="firstName"
                                   aria-describedby="firstNameFeedback" value="${requestScope.user.firstName}">
                            <div id="firstNameFeedback" class="invalid-feedback"></div>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="lastName">Last name</label>
                            <input type="text" name="lastName" class="form-control is-valid" id="lastName"
                                   aria-describedby="lastNameFeedback" value="${requestScope.user.lastName}">
                            <div id="lastNameFeedback" class="invalid-feedback"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="tel" class="form-control" id="phone" readonly value="${requestScope.user.account.phone}">
                    </div>
                    <div class="form-group">
                        <label for="email">Email<span class="text-muted">(Optional)</span></label>
                        <input type="email" name="email" class="form-control is-valid" id="email" aria-describedby="emailFeedback"
                               placeholder="example@gmail.com" value="${requestScope.user.email.orElse("")}">
                        <div id="emailFeedback" class="invalid-feedback"></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancel</button>
                <button id="submitButton" type="submit" form="editPersonalInfoForm" class="btn btn-primary">Save
                    changes
                </button>
            </div>
        </div>
    </div>
</div>
