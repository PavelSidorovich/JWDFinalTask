<%@ page contentType="text/html;charset=UTF-8" %>

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
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" id="approveButton" class="btn btn-primary">Confirm</button>
            </div>
        </div>
    </div>
</div>
