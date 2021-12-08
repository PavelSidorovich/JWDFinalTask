$(document).ready(function () {
  $.post("/controller?command=get_pie_chart", function (data) {
    showPieChart(data.obj);
  });
});

function showPieChart(map) {
  const data = {
    labels: [
      'Cancelled',
      'Waiting driver response',
      'In process',
      'Completed',
    ],
    datasets: [{
      label: 'My First Dataset',
      data: [map.CANCELLED, map.NEW, map.IN_PROCESS, map.COMPLETED],
      backgroundColor: [
        'rgb(255, 99, 132)',
        'rgb(255, 205, 86)',
        'rgb(54, 162, 235)',
        'rgb(0, 210, 0)'
      ],
      hoverOffset: 4
    }]
  };

  Chart.defaults.font.size = 30;
  const ctx = document.getElementById('pieChart').getContext('2d');
  const myChart = new Chart(ctx, {
    type: 'doughnut',
    data: data,
  });
}