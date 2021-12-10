<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="driver" scope="request" type="com.sidorovich.pavel.buber.api.model.Driver"/>
<script src="${contextPath}/js/driverValidator.js?v=1.0"></script>
<script src="${contextPath}/js/editDriverInfo.js?v=1.0"></script>
<%--<fmt:setLocale value="${cookie.lang.value}"/>--%>
<%--<fmt:setBundle basename="l10n.partials.newBonusModal" var="loc"/>--%>
<%--<fmt:message bundle="${loc}" key="label.modal.title" var="modalTitleLabel"/>--%>
<%--<fmt:message bundle="${loc}" key="label.placeholder.discount" var="discountLabel"/>--%>
<%--<fmt:message bundle="${loc}" key="label.dateOfExpiration" var="expirationDateLabel"/>--%>
<%--<fmt:message bundle="${loc}" key="label.button.deselectAll" var="deselectButtonLabel"/>--%>
<%--<fmt:message bundle="${loc}" key="label.button.selectAll" var="selectButtonLabel"/>--%>
<%--<fmt:message bundle="${loc}" key="label.button.cancel" var="cancelButtonLabel"/>--%>
<%--<fmt:message bundle="${loc}" key="label.button.submit" var="submitButtonLabel"/>--%>

<div id="driverInfoModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Driver info</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="alert alert-warning" role="alert">
                        <strong>Warning:</strong> After resending application you will not be able to process orders
                        until administrator check it.
                    </div>
                    <div class="row">
                        <div id="photoContainer" class="col-md-7">
                            <img id="taxiPhoto" src="../../../images/taxes/${driver.taxi.photoFilepath}"
                                 class="img-fluid"
                                 alt="taxi photo">
                        </div>
                        <div class="col-md-5">
                            <form id="driverInfoForm" action="${contextPath}/controller?command=edit_driver_info"
                                  method="post" enctype="multipart/form-data" novalidate>
                                <div class="form-group">
                                    <label for="drivingLicenceSerial">Driving licence serial number</label>
                                    <input type="text" name="drivingLicenceSerial" class="form-control is-valid"
                                           id="drivingLicenceSerial"
                                           aria-describedby="drivingLicenceSerialFeedback"
                                           value="${driver.drivingLicence}" placeholder="1AA 111111">
                                    <div id="drivingLicenceSerialFeedback" class="invalid-feedback"></div>
                                </div>
                                <div class="form-group">
                                    <label for="carBrand">Car brand</label>
                                    <input type="text" name="carBrand" class="form-control is-valid" id="carBrand"
                                           aria-describedby="carBrandFeedback" value="${driver.taxi.carBrand}">
                                    <div id="carBrandFeedback" class="invalid-feedback"></div>
                                </div>
                                <div class="form-group">
                                    <label for="carModel">Car brand</label>
                                    <input type="text" name="carModel" class="form-control is-valid" id="carModel"
                                           aria-describedby="carModelFeedback" value="${driver.taxi.carModel}">
                                    <div id="carModelFeedback" class="invalid-feedback"></div>
                                </div>
                                <div class="form-group">
                                    <label for="carLicencePlate">Car licence plate</label>
                                    <input type="text" name="carLicencePlate" class="form-control is-valid"
                                           id="carLicencePlate"
                                           aria-describedby="carLicencePlateFeedback"
                                           value="${driver.taxi.licencePlate}" placeholder="1111 AA-7">
                                    <div id="carLicencePlateFeedback" class="invalid-feedback"></div>
                                </div>
                                <div class="mb-3">
                                    <input type="file" name="carPhoto" class="file" accept=".jpeg,.png,.jpg" hidden>
                                    <label for="carPhotoInput">New car photo</label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" disabled placeholder="Upload file"
                                               id="carPhotoInput">
                                        <div id="browseButton" class="input-group-append">
                                            <button type="button" class="browse btn btn-primary">Browse...</button>
                                        </div>
                                        <div class="invalid-feedback"></div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" id="submitButton" form="driverInfoForm" class="btn btn-primary">Save changes
                </button>
            </div>
        </div>
    </div>
</div>