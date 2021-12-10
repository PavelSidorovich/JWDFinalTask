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
    const status = $statusCheckbox.is(":checked") ? "FREE" : "REST";

    $.post("/controller?command=update_driver_status",
      {id: $("#driverId").val(), driverStatus: status}
    );
    editStatusLine();
  })
}

function editStatusLine() {
  const $statusLine = $("#statusLine");

  if ($("#statusCheckbox").is(":checked")) {
    $statusLine.text("Waiting for orders");
    $statusLine.after('<div id="statusSpinner" class="spinner-grow text-warning ml-2" role="status"></div>');
  } else {
    $statusLine.text("You are taking rest...");
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
    $("#modalHeader").prepend("<h5>Approve cancelling</h5>");
    $("#modalMessage").text("Are you really want to cancel order?");
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=driver_process_order" method="post">' +
      '<input type="text" name="orderStatus" value="CANCELLED" hidden>' +
      '<button class="btn btn-danger btn-block" type="submit">Cancel order</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}

function addTakeButtonListener() {
  $("#takeButton").click(function () {
    clearApproveModal();
    $("#modalHeader").prepend("<h5>Approve taking order</h5>");
    $("#modalMessage").text("Are you really want take this order?");
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=driver_process_order" method="post">' +
      '<input type="text" name="orderStatus" value="IN_PROCESS" hidden>' +
      '<button class="btn btn-success btn-block" type="submit">Take order</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}

function addConfirmButtonListener() {
  $("#confirmOrderButton").click(function () {
    clearApproveModal();
    $("#modalHeader").prepend("<h5>Approve confirming payment</h5>");
    $("#modalMessage").text("Are you really want confirm the payment?");
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=confirm_payment" method="post">' +
      '<button class="btn btn-success btn-block" type="submit">Confirm payment</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}