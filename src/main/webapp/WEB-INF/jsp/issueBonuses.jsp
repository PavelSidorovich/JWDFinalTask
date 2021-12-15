<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.issueBonuses" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.button.newBonus" var="newBonusButtonLabel"/>
<fmt:message bundle="${loc}" key="label.table.noData" var="noDataLabel"/>
<fmt:message bundle="${loc}" key="label.table.fNameColumn" var="fNameColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.lNameColumn" var="lNameColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.phoneColumn" var="phoneColumnLabel"/>
<fmt:message bundle="${loc}" key="label.table.discountColumn" var="discountLabel"/>
<fmt:message bundle="${loc}" key="label.table.expiryDateColumn" var="expiryDateLabel"/>
<fmt:message bundle="${loc}" key="label.table.controlColumn" var="controlColumnLabel"/>
<fmt:message bundle="${loc}" key="label.modal.header" var="modalHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.modal.body" var="modalBodyLabel"/>
<fmt:message bundle="${loc}" key="label.table.first" var="firstLabel"/>
<fmt:message bundle="${loc}" key="label.table.prev" var="prevLabel"/>
<fmt:message bundle="${loc}" key="label.table.next" var="nextLabel"/>
<fmt:message bundle="${loc}" key="label.table.last" var="lastLabel"/>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
    <link href="${contextPath}/css/tabulator_modern.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/bonusesControl.js?v=1.5" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator/luxon.js?v=1.0" type="text/javascript"></script>
    <script src="${contextPath}/js/tabulator/tabulator.min.js?v=1.0" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/adminNavBar.jsp"/>
<div class="container-fluid mt-3">
    <div class="row p-3">
        <jsp:include page="partials/bonusFilter.jsp"/>
        <div class="col-md-9 order-md-2">
            <div class="container">
                <div class="row">
                    <h2 class="order-1 mr-2">${headerLabel}</h2>
                    <button type="button" id="newBonusButton" class="btn btn-primary order-2"
                            data-toggle="modal" data-target="#bonusModal">${newBonusButtonLabel}
                    </button>
                </div>
                <br>
            </div>
            <div id="table"></div>
        </div>
    </div>
</div>
<script>
  function createTable(bonuses) {
    return new Tabulator("#table", {
      data: bonuses,
      locale: true,
      layout: "fitColumns",
      responsiveLayout: "hide",
      addRowPos: "top",
      history: false,
      pagination: true,
      paginationButtonCount: "5",
      paginationSize: 11,
      movableColumns: false,
      resizableRows: true,
      placeholder: "${noDataLabel}",
      langs: {
        "ru-ru": {
          "pagination": {
            "first": "${firstLabel}",
            "last": "${lastLabel}",
            "prev": "${prevLabel}",
            "next": "${nextLabel}",
          },
        },
      },
      initialSort: [
        {column: "discount", dir: "asc"},
      ],
      columns: [
        {title: "${fNameColumnLabel}", field: "client.firstName", hozAlign: "center"},
        {title: "${lNameColumnLabel}", field: "client.lastName", hozAlign: "center"},
        {title: "${phoneColumnLabel}", field: "client.account.phone", hozAlign: "center", width: 170},
        {title: "${discountLabel}", field: "discount", hozAlign: "center"},
        {
          title: "${expiryDateLabel}",
          field: "expires",
          hozAlign: "center",
          width: 200,
          formatter: "datetime",
          formatterParams: {
            inputFormat: "MMM d, yyyy",
            outputFormat: "dd/MM/yyyy",
            invalidPlaceholder: "(invalid date)",
          }
        },
        {
          title: "${controlColumnLabel}",
          hozAlign: "center",
          width: 150,
          formatter: printButton,
          cellClick: function (e) {
            showModal(e, "${modalHeaderLabel}", "${modalBodyLabel}");
          }
        },
      ],
    });
  }
</script>
<jsp:include page="partials/commonFooter.jsp"/>
<jsp:include page="partials/approveActionModal.jsp"/>
<jsp:include page="partials/newBonusModal.jsp"/>
</body>
</html>
