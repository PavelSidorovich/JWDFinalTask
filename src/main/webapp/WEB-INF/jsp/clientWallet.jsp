<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwds" uri="com.sidorovich.pavel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.clientWallet" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.card.header" var="cardHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.card.rubles" var="rublesLabel"/>
<fmt:message bundle="${loc}" key="label.card.debits" var="transactionsLabel"/>
<fmt:message bundle="${loc}" key="label.button.topUpBalance" var="topUpBalanceButton"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <link href="${contextPath}/css/wallet.css?v=1.0" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js" rel="stylesheet"></script>
    <script src="${contextPath}/js/topUpBalance.js?v=1.1" type="text/javascript"></script>
</head>

<body>
<jsp:include page="partials/userNavBar.jsp"/>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 order-md-2">
            <img class="d-block mx-auto" src="${contextPath}/images/piggy-bank.svg" alt="piggy bank" width="400">
        </div>
        <div class="col-md-6 order-md-1 card-deck mb-3 text-center">
            <div class="card mb-4 shadow-sm">
                <div class="card-header">
                    <h4 class="my-0 font-weight-normal">${cardHeaderLabel}</h4>
                </div>
                <div class="card-body">
                    <h1 class="card-title pricing-card-title">${requestScope.cash} <small class="text-muted">${rublesLabel}</small>
                    </h1>
                    <jwds:transactionsDisplayer title="${transactionsLabel}" symbol="-"
                                                transactions="${requestScope.debits}" currency="${rublesLabel}"/>
                    <button type="button" class="btn btn-lg btn-block btn-outline-primary" data-toggle="modal"
                            data-target="#topUpBalanceModal">
                        <i class="bi bi-arrow-up"></i>
                        <svg xmlns="/images/arrow-up.svg" width="16" height="16" fill="currentColor"
                             class="bi bi-arrow-up" viewBox="0 0 16 16">
                            <path fill-rule="evenodd"
                                  d="M8 15a.5.5 0 0 0 .5-.5V2.707l3.146 3.147a.5.5 0 0 0 .708-.708l-4-4a.5.5 0 0 0-.708 0l-4 4a.5.5 0 1 0 .708.708L7.5 2.707V14.5a.5.5 0 0 0 .5.5z"></path>
                        </svg>
                        ${topUpBalanceButton}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="partials/topUpBalanceModal.jsp"/>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
