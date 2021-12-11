<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.adminSideBar" var="loc"/>
<fmt:message bundle="${loc}" key="label.header" var="headerLabel"/>
<fmt:message bundle="${loc}" key="label.link.pieChart" var="pieChartLink"/>
<fmt:message bundle="${loc}" key="label.link.lineChart" var="lineChartLink"/>

<nav id="sidebarMenu" class="col-md-3 col-lg-3 d-md-block bg-light sidebar collapse">
    <div class="sidebar-sticky pt-3">
        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>${headerLabel}</span>
            <a class="d-flex align-items-center text-muted" href="#" aria-label="Add a new report">
                <span data-feather="plus-circle"></span>
            </a>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_pie_chart">
                    <span data-feather="file-text"></span>
                    ${pieChartLink}
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_line_chart">
                    <span data-feather="file-text"></span>
                    ${lineChartLink}
                </a>
            </li>
        </ul>
    </div>
</nav>