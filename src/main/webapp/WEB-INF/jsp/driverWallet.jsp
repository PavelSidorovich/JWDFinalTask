<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwds" uri="com.sidorovich.pavel" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setBundle basename="l10n.page.clientWallet" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.card.header" var="cardHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.card.rubles" var="rublesLabel"/>
<fmt:message bundle="${loc}" key="label.card.credits" var="transactionsLabel"/>
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
</head>

<body>
<jsp:include page="partials/driverNavBar.jsp"/>
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
                    <jwds:transactionsDisplayer title="${transactionsLabel}" symbol="+"
                                                transactions="${requestScope.credits}" currency="${rublesLabel}"/>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="partials/commonFooter.jsp"/>
</body>
</html>
