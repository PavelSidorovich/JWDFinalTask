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
    $("#modalHeader").prepend("<h5>Approve cancelling</h5>");
    $("#modalMessage").text("Are you really want to cancel order? (bonus will be lost)");
    $("#approveButton").remove();
    $("#modalButtons").append(
      '<form action="/controller?command=cancel_order" method="post">' +
      '<button class="btn btn-danger btn-block" type="submit">Cancel order</button>' +
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
  $price.text("Price: " + price + " RUB");
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

//
// function disableFormFields() {
//   let $cancelOrderButton = $("#cancelOrderButton");
//   let $callTaxiButton = $("#callTaxiButton");
//
//   $("#latitudeTo").prop("disabled", true);
//   $("#longitudeTo").prop("disabled", true);
//   $("#taxis").prop("disabled", true);
//   $("#bonuses").prop("disabled", true);
//   $cancelOrderButton.prop("disabled", false);
//   $callTaxiButton.prop("disabled", true);
//   $cancelOrderButton.show("slow");
//   $callTaxiButton.hide("slow");
// }
//
// function enableFormFields() {
//
// }
//
// function fillOrderInfo(order) {
//   $("#latitudeTo").val(order.endCoordinates.latitude);
//   $("#longitudeTo").val(order.endCoordinates.longitude);
//   displayPrice(order.price);
//   $("#taxis").append('<option selected>' + order.driver.taxi.carBrand + " "
//     + order.driver.taxi.carModel + " ("
//     + order.driver.taxi.licencePlate + ")" + '</option>');
//   // $("#bonuses").prop("disabled", true);
// }
//
// function getOrderIfExists() {
//   let $spinner = $("#spinner");
//
//   $.post("/controller?command=get_my_order", {phone: $("#phone").val()}, function (data) {
//     console.log(data.obj);
//     if (data.obj === undefined) {
//       enableFormFields();
//       getFreeTaxis();
//       getUserBonuses();
//       $spinner.hide();
//       $("#cancelOrderButton").hide();
//     } else {
//       fillOrderInfo(data.obj);
//       $spinner.removeAttr("class");
//       if (data.obj.status === "NEW") {
//         disableFormFields();
//         $("#orderStatus").text("Waiting for driver response");
//         $spinner.attr("class", "spinner-grow text-warning ml-2");
//         $spinner.show();
//         $("#callTaxiButton").hide();
//         $("#cancelOrderButton").show();
//       } else if (data.obj.status === "IN_PROCESS") {
//         disableFormFields();
//         $spinner.attr("class", "spinner-grow text-success ml-2");
//         $spinner.show();
//       } else {
//         $spinner.hide();
//       }
//     }
//   }, "json");
// }
//


//
// function fillTaxesOption(taxis) {
//   $(".taxiToDelete").remove();
//   taxis.forEach((taxi) => {
//     $("#taxis").append('<option class="taxiToDelete" value="' + taxi.licencePlate + '">'
//       + taxi.carBrand + ' ' + taxi.carModel + ' (' + taxi.licencePlate + ')</option>'
//     );
//   });
// }
//
// function getFreeTaxis() {
//   $.post("/controller?command=get_free_taxis", function (data) {
//     if (data.status === "ERROR") {
//       const alertDiv = createAlert(data);
//
//       $("#orderForm").before(alertDiv);
//     }
//     fillTaxesOption(data.obj);
//   }, "json");
// }
//
// function fillUserBonuses(bonuses) {
//   $(".bonusToDelete").remove();
//   console.log(bonuses);
//   bonuses.forEach((bonus) => {
//     $("#bonuses").append('<option class="bonusToDelete" value="' + bonus.discount + '">'
//       + bonus.discount + '% ' + 'discount</option>'
//     );
//   });
// }
//
// function getUserBonuses() {
//   $.post("/controller?command=get_my_bonuses", {phone: $("#phone").val()}, function (data) {
//     fillUserBonuses(data.obj);
//   }, "json");
// }
//
// function createAlert(data) {
//   let alertDiv = $('<div class="alert alert-warning alert-dismissible fade show"></div>')
//   alertDiv.append($("<strong></strong>").text(data.message));
//
//   return alertDiv;
// }
//
// function showErrorMessage($field, errorMsg) {
//   $field.text(errorMsg);
//   $field.show("slow");
// }
//
// // function showErrorMessage($field, errorMsg) {
// //   let invalidFeedbackId = "#" + $field.attr("aria-describedby");
// //   let $invalidFeedbackDiv = $(invalidFeedbackId);
// //
// //   $invalidFeedbackDiv.text(errorMsg);
// //   $invalidFeedbackDiv.show("slow");
// // }
//
// function showInvalidFields(data) {
//   let divs = $(".invalid-feedback");
//
//   divs.text("")
//   divs.hide();
//   if (data.taxi) {
//     showErrorMessage($("#validationTaxiFeedback"), data.taxi);
//   }
//   if (data.endLatitude) {
//     showErrorMessage($("#endLatitudeFeedback"), data.endLatitude);
//   }
//   if (data.endLongitude) {
//     showErrorMessage($("#endLongitudeFeedback"), data.endLongitude);
//   }
//   if (data.endCoordinates) {
//     showErrorMessage($("#endLongitudeFeedback"), data.endCoordinates);
//   }
// }
