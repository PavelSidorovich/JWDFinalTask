<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.driverModal" var="loc"/>
<fmt:message bundle="${loc}" key="label.comment" var="commentLabel"/>
<fmt:message bundle="${loc}" key="label.placeholder.comment" var="commentPlaceholderLabel"/>

<div id="driverApplicationModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div id="modalHeader" class="modal-header"></div>
            <div id="modalBody" class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div id="photoContainer" class="col-7"></div>
                        <div id="infoContainer" class="col-5"></div>
                    </div>
                    <div id="commentBlock">
                        <label for="validationTextarea">${commentLabel}</label>
                        <textarea class="form-control" style="resize: none" id="validationTextarea" rows="5"
                                  placeholder="${commentPlaceholderLabel}"></textarea>
                    </div>
                </div>
            </div>
            <div id="modalButtons" class="modal-footer"></div>
        </div>
    </div>
</div>