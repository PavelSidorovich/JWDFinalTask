$(document).ready(function () {
  addCashValidator();
});

function addCashValidator() {
  const $cashFeedback = $("#cashFeedback");
  const $cash = $("#cash");
  const cashRegex = new RegExp('^\\d{1,6}[.]?\\d{0,2}$');

  function showErrorIfInvalid() {
    if ($cash.val().match(cashRegex)) {
      $cash.addClass("is-valid");
      $cash.removeClass("is-invalid");
      $cashFeedback.hide("slow");
      $("#submitButton").attr("disabled", false);
    } else {
      $cash.addClass("is-invalid");
      $cash.removeClass("is-valid");
      $cashFeedback.text(invalidCashMsg);
      $cashFeedback.show("slow");
      $("#submitButton").attr("disabled", true);
    }
  }

  showErrorIfInvalid();
  $cash.on("input", function () {
    showErrorIfInvalid();
  });
}