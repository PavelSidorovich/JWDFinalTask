$(document).ready(function () {
  $("#ru").click(function () {
    document.cookie = "lang=ru_RU";
    location.reload();
  })
  $("#en").click(function () {
    document.cookie = "lang=en_US";
    location.reload();
  })
  $("#zh").click(function () {
    document.cookie = "lang=zh_TW";
    location.reload();
  })
});