$(document).ready(function () {
  addLoginFormListener();
});

function addLoginFormListener() {
  $("#loginForm").submit(function (event) {
    event.preventDefault();
    console.log($(this).serialize());
    $.post(
      $(this).attr("action"),
      $(this).serialize(),
      function (data) {
        if (data.status === "ERROR") {
          $('#errorLoginMsg').text(data.message);
        } else {
          window.location.replace(data.path);
        }
      });
  });
}
