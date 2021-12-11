$(document).ready(function () {
  $.post("/controller?command=get_line_chart", function (data) {
    showLineChart(data.obj);
  });
});