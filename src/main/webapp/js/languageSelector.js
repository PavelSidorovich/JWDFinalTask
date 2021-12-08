$(document).ready(function () {
  $("#ru").click(function () {
    document.cookie = "lang=ru_RU"
    location.reload();
  })
  $("#en").click(function () {
    document.cookie = "lang=en_US"
    location.reload();
  })
  $("#ch").click(function () {
    $.cookie("lang", "ch_US");
    location.reload();
  })
});