<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.userControl" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitleLabel"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.table.noData" var="noDataLabel"/>
<fmt:message bundle="${loc}" key="label.table.roleColumn" var="roleColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.fNameColumn" var="fNameColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.lNameColumn" var="lNameColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.phoneColumn" var="phoneColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.emailColumn" var="emailColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.cashColumn" var="cashColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.statusColumn" var="statusColumnLabel"/>
<fmt:message bundle="${loc}" key="label.button.blocked" var="blockedButtonLabel"/>
<fmt:message bundle="${loc}" key="label.button.active" var="activeButtonLabel"/>
<fmt:message bundle="${loc}" key="label.table.first" var="firstLabel"/>
<fmt:message bundle="${loc}" key="label.table.prev" var="prevLabel"/>
<fmt:message bundle="${loc}" key="label.table.next" var="nextLabel"/>
<fmt:message bundle="${loc}" key="label.table.last" var="lastLabel"/>
<fmt:message bundle="${loc}" key="label.successMsg" var="successMsgLabel"/>
<fmt:message bundle="${loc}" key="label.driverRole" var="driverRoleLabel"/>
<fmt:message bundle="${loc}" key="label.clientRole" var="clientRoleLabel"/>

<html>
<head>
    <title>${pageTitleLabel}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/userControl.js?v=1.3" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="container-fluid mt-3">
    <div class="row p-3">
        <jsp:include page="partials/userFilter.jsp"/>
        <div class="col-md-9 order-md-2">
            <h2>${headerLabel}</h2>
            <br>
            <div id="table"></div>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
<script>
  const driverRoleLabel = '${driverRoleLabel}';
  const clientRoleLabel = '${clientRoleLabel}';
  const noDataLabel = '${noDataLabel}';
  const roleColumnLabel = '${roleColumnLabel}';
  const fNameColumnLabel = '${fNameColumnLabel}';
  const lNameColumnLabel = '${lNameColumnLabel}';
  const phoneColumnLabel = '${phoneColumnLabel}';
  const emailColumnLabel = '${emailColumnLabel}';
  const cashColumnLabel = '${cashColumnLabel}';
  const statusColumnLabel = '${statusColumnLabel}';
  const blockedButtonLabel = '${blockedButtonLabel}';
  const activeButtonLabel = '${activeButtonLabel}';
  const firstLabel = '${firstLabel}';
  const prevLabel = '${prevLabel}';
  const nextLabel = '${nextLabel}';
  const lastLabel = '${lastLabel}';
  const successfulUpdateLabel = '${successMsgLabel}';
</script>
</body>
</html>
