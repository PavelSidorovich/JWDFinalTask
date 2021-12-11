$(document).ready(function () {
  $(".invalid-feedback").show("slow");
  addTaxiListener();
  addCancelButtonListener();
});

function clearApproveModal() {
  $("#modalHeader").find("h5").remove();
  $("#approveButton").remove();
  $("#modalButtons").find("form").remove();
}

function addCancelButtonListener() {
  $("#cancelButton").click(function () {
    clearApproveModal();
    $("#modalHeader").prepend("<h5>" + approveCancellingHeader + "</h5>");
    $("#modalMessage").text(approveCancelling);
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=cancel_order" method="post">' +
      '<button class="btn btn-danger btn-block" type="submit">' + cancelButtonLabel + '</button>' +
      ' </form>'
    );
    $("#modalApprove").modal();
  });
}

function addTaxiListener() {
  $("#taxis").change(function () {
    $.post("/controller?command=get_taxi_photo", {licencePlate: $("#taxis").val()},
      function (data) {
        let $taxiPreview = $("#taxiPreview");

        if (data.obj === undefined) {
          $taxiPreview.attr("src", "");
        }
        $taxiPreview.attr("src", data.obj);
      }, "json");
  })
}

function getOrderPrice() {
  let $price = $("#price");

  $.post("/controller?command=get_order_price",
    {
      initialLatitude: $("#latitudeFrom").val(),
      initialLongitude: $("#longitudeFrom").val(),
      endLatitude: $("#latitudeTo").val(),
      endLongitude: $("#longitudeTo").val(),
      bonus: $("#bonuses").val(),
    },
    function (data) {
      console.log(data.obj);
      if (data.status === "ERROR") {
        $price.hide("slow");
      } else {
        displayPrice(data.obj);
      }
    }, "json");
}

function displayPrice(price) {
  let $price = $("#price");

  $price.show("slow");
  $price.text(priceLabel + " " + price + " " + currencyLabel);
}

function addPriceParametersListener() {
  getOrderPrice();
  $("#longitudeTo").change(function () {
    getOrderPrice();
  });
  $("#latitudeTo").change(function () {
    getOrderPrice();
  });
  $("#bonuses").change(function () {
    getOrderPrice();
  });
}

function showInfoModal() {
  $("#infoModal").modal();
}
