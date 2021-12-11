<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.lineChart" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.chartLabel" var="chartLabel"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <link href="${contextPath}/css/dashboard.css?v=1.1" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/lineChart.js?v=1.1"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="container-fluid">
    <div class="row">
        <c:import url="partials/adminSideBar.jsp"/>
        <main role="main" class="col-md-9 ml-sm-auto col-lg-9 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2">${headerLabel}</h1>
            </div>
            <canvas class="my-4 w-100" id="lineChart" width="900" height="380"></canvas>
        </main>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
<script>
  function showLineChart(map) {
    const labels = Object.keys(map);
    const values = Object.values(map);
    const data = {
      labels: labels,
      datasets: [{
        label: "${chartLabel}",
        data: values,
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(255, 159, 64, 0.2)',
          'rgba(255, 205, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(201, 203, 207, 0.2)'
        ],
        borderColor: [
          'rgb(255, 99, 132)',
          'rgb(255, 159, 64)',
          'rgb(255, 205, 86)',
          'rgb(75, 192, 192)',
          'rgb(54, 162, 235)',
          'rgb(153, 102, 255)',
          'rgb(201, 203, 207)'
        ],
        borderWidth: 1,
        base: 0,
      }]
    };

    const ctx = document.getElementById('lineChart').getContext('2d');
    const myChart = new Chart(ctx, {
      labels: labels,
      data: data,
      type: 'bar',
      plugins: {
        legend: {
          position: 'top',
          labels: {
            font: {
              size: 40,
            }
          }
        },
      },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              fontSize: 20,
              beginAtZero: true,
              stepSize: 1
            }
          }],
          xAxes: [{
            ticks: {
              fontSize: 20,
            }
          }],
        },
      }
    });
  }
</script>
</body>
</html>

