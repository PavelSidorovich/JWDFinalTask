<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<nav id="sidebarMenu" class="col-md-3 col-lg-3 d-md-block bg-light sidebar collapse">
    <div class="sidebar-sticky pt-3">
        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Statistics</span>
            <a class="d-flex align-items-center text-muted" href="#" aria-label="Add a new report">
                <span data-feather="plus-circle"></span>
            </a>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_pie_chart">
                    <span data-feather="file-text"></span>
                    Ratio of order statuses
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=show_line_chart">
                    <span data-feather="file-text"></span>
                    Amount of orders for last 7 days
                </a>
            </li>
        </ul>
    </div>
</nav>