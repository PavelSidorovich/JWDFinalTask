$(document).ready(function () {
  addDiscountValidator();
});

function checkField($field, regex) {
  if ($field.val().match(regex) && $field.val() <= 100 && $field.val() >= 1) {
    $field.addClass("is-valid");
    $field.removeClass("is-invalid");

    return true;
  } else {
    $field.addClass("is-invalid");
    $field.removeClass("is-valid");

    return false;
  }
}

function addDiscountValidator() {
  const $discount = $('[name="discount"]');
  const discountRegex = new RegExp('^\\d[0-9]{1,2}([,.]\\d{0,2})?$');

  $discount.on("input", function () {
    checkField($discount, discountRegex);
  });
}