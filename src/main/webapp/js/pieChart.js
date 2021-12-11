$(document).ready(function () {
  $.post("/controller?command=get_pie_chart", function (data) {
    showPieChart(data.obj);
  });
});
