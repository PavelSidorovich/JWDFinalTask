<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwds" uri="com.sidorovich.pavel" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.clientWallet" var="loc"/>
<fmt:message bundle="${loc}" key="label.page.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.alt.bankPhoto" var="piggyBankLabel"/>
<fmt:message bundle="${loc}" key="label.card.header" var="cardHeaderLabel"/>
<fmt:message bundle="${loc}" key="label.card.rubles" var="rublesLabel"/>
<fmt:message bundle="${loc}" key="label.card.credits" var="transactionsLabel"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">    <link href="${contextPath}/css/applications.css?v=1.0" rel="stylesheet" type="text/css">
    <link href="${contextPath}/css/wallet.css?v=1.0" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
</head>

<body>
<jsp:include page="partials/driverNavBar.jsp"/>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 order-md-2">
            <img class="d-block mx-auto" src="${contextPath}/images/piggy-bank.svg" alt="${piggyBankLabel}" width="400">
        </div>
        <div class="col-md-6 order-md-1 card-deck mb-3 text-center">
            <div class="card mb-4 shadow-sm">
                <div class="card-header">
                    <h4 class="my-0 font-weight-normal">${cardHeaderLabel}</h4>
                </div>
                <div class="card-body">
                    <h1 class="card-title pricing-card-title">${requestScope.cash} <small
                            class="text-muted">${rublesLabel}</small>
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
