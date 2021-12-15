<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.driverControl" var="local"/>
<fmt:message bundle="${local}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${local}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${local}" key="label.prompt" var="promptLabel"/>
<fmt:message bundle="${local}" key="label.placeholder" var="placeholderLabel"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/driverControl.js?v=1.7" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="album py-5 bg-white">
    <div class="container">
        <h2>${headerLabel}<span id="filterCount" class="badge badge-secondary badge-pill"></span></h2>
        <p>${promptLabel}</p>
        <label for="searchInput"></label>
        <input class="form-control" id="searchInput" type="text" placeholder="${placeholderLabel}">
        <br>
        <div id="cardContainer" class="row"></div>
    </div>
</div>
<jsp:include page="partials/driverModal.jsp"/>
<jsp:include page="partials/commonFooter.jsp"/>
<script>
  const taxiPhotoAlt = '<fmt:message bundle="${local}" key="label.taxiPhotoAlt"/>';
  const failMsg = '<fmt:message bundle="${local}" key="label.failMsg"/>';
  const successMsg = '<fmt:message bundle="${local}" key="label.successMsg"/>';
  const detailsButtonLabel = '<fmt:message bundle="${local}" key="label.button.details"/>';
  const driverLabel = '<fmt:message bundle="${local}" key="label.driver"/>';
  const fNameLabel = '<fmt:message bundle="${local}" key="label.driver.fName"/>';
  const lNameLabel = '<fmt:message bundle="${local}" key="label.driver.lName"/>';
  console.log('<fmt:message bundle="${local}" key="label.driver.lName"/>')
  const phoneLabel = '<fmt:message bundle="${local}" key="label.driver.phone"/>';
  const emailLabel = '<fmt:message bundle="${local}" key="label.driver.email"/>';
  const rejectButtonLabel = '<fmt:message bundle="${local}" key="label.button.reject"/>';
  const approveButtonLabel = '<fmt:message bundle="${local}" key="label.button.approve"/>';
  const closeButtonLabel = '<fmt:message bundle="${local}" key="label.button.close"/>';
  const drivingLicenceLabel = '<fmt:message bundle="${local}" key="label.drivingLicence"/>';
  const driverInfoLabel = '<fmt:message bundle="${local}" key="label.driverInfo"/>';
  const positionLabel = '<fmt:message bundle="${local}" key="label.position"/>';
  const latitudeLabel = '<fmt:message bundle="${local}" key="label.latitude"/>';
  const longitudeLabel = '<fmt:message bundle="${local}" key="label.longitude"/>';
  const pendingLabel = '<fmt:message bundle="${local}" key="label.pending"/>';
  const rejectedLabel = '<fmt:message bundle="${local}" key="label.rejected"/>';
  const freeLabel = '<fmt:message bundle="${local}" key="label.free"/>';
  const busyLabel = '<fmt:message bundle="${local}" key="label.busy"/>';
  const restLabel = '<fmt:message bundle="${local}" key="label.rest"/>';
</script>
</body>
</html>
