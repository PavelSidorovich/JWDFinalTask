$(document).ready(function () {
  addStatusListener();
  getDriverStatus();
  addCancelButtonListener();
  addTakeButtonListener();
  addConfirmButtonListener();
});

function addStatusListener() {
  let $statusCheckbox = $("#statusCheckbox");

  $statusCheckbox.change(function () {
    $.post("/controller?command=toggle_driver_status",
      {isResting: $statusCheckbox.is(":checked")}, function (data) {
        if (data.status === "ERROR") {
          location.reload();
        }
      }
    );
    editStatusLine();
  })
}

function editStatusLine() {
  const $statusLine = $("#statusLine");

  if ($("#statusCheckbox").is(":checked")) {
    $statusLine.text(freeStatus);
    $statusLine.after('<div id="statusSpinner" class="spinner-grow text-warning ml-2" role="status"></div>');
  } else {
    $statusLine.text(restStatus);
    $("#statusSpinner").remove();
  }
}

function getDriverStatus() {
  $.post("/controller?command=get_driver_status", {id: $("#driverId").val()},
    function (data) {
      if (data.status === "SUCCESS") {
        if (data.obj === "FREE") {
          $("#statusCheckbox").attr("checked", true);
        } else {
          $("#statusCheckbox").attr("checked", false);
        }
      }
    }
  );
}

function clearApproveModal() {
  $("#modalHeader").find("h5").remove();
  $("#approveButton").remove();
  $("#modalButtons").find("form").remove();
}

function addCancelButtonListener() {
  $("#cancelButton").click(function () {
    clearApproveModal();
    $("#modalHeader").prepend("<h5>" + approveCancelHeader + "</h5>");
    $("#modalMessage").text(approveCancelling);
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=driver_process_order" method="post">' +
      '<input type="text" name="orderStatus" value="CANCELLED" hidden>' +
      '<button class="btn btn-danger btn-block" type="submit">' + cancelButtonLabel + '</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}

function addTakeButtonListener() {
  $("#takeButton").click(function () {
    clearApproveModal();
    $("#modalHeader").prepend("<h5>" + approveTakingHeader + "</h5>");
    $("#modalMessage").text(approveTaking);
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=driver_process_order" method="post">' +
      '<input type="text" name="orderStatus" value="IN_PROCESS" hidden>' +
      '<button class="btn btn-success btn-block" type="submit">' + takeButtonLabel + '</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}

function addConfirmButtonListener() {
  $("#confirmOrderButton").click(function () {
    clearApproveModal();
    $("#modalHeader").prepend("<h5>" + confirmPaymentHeader + "</h5>");
    $("#modalMessage").text(confirmPayment);
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=confirm_payment" method="post">' +
      '<button class="btn btn-success btn-block" type="submit">' + confirmButtonLabel + '</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}