<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<link href="${contextPath}/css/menu.css?v=1.0" rel="stylesheet">

<nav class="navbar sticky-top navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Buber.Taxi</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText"
            aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarText">
        <ul id="menu" class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${contextPath}/controller?command=show_user_control">Manage users</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${contextPath}/controller?command=driver_applications">Incoming driver applications</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Issue bonuses</a>
            </li>
        </ul>
        <div class="dropdown navbar-toggler-icon">
            <a class="nav-link" data-toggle="dropdown" href="#" role="button" aria-expanded="false"></a>
            <div class="dropdown-menu right-align">
                <a class="dropdown-item" href="#">Action</a>
                <a class="dropdown-item" href="#">Another action</a>
                <a class="dropdown-item" href="#">Something else here</a>
                <div class="dropdown-divider"></div>
                <c:if test="${not empty sessionScope.user}">
                    <a class="dropdown-item text-danger" href="${contextPath}/controller?command=logout">Logout</a>
                </c:if>
            </div>
        </div>
    </div>

</nav>


<%--    <nav class="my-2 my-md-0 mr-md-3">--%>
<%--        <c:if test="${empty sessionScope.user}">--%>
<%--            <a class="p-2 text-dark" href="${contextPath}/controller?command=show_driver_register">Become a driver</a>--%>
<%--        </c:if>--%>
<%--    </nav>--%>
<%--    <c:choose>--%>
<%--        <c:when test="${not empty sessionScope.user}">--%>
<%--            <a class="btn btn-outline-primary" href="${contextPath}/controller?command=logout">Logout</a>--%>
<%--        </c:when>--%>
<%--        <c:otherwise>--%>
<%--            <a class="btn btn-outline-primary" href="${contextPath}/controller?command=show_login">Sign in</a>--%>
<%--        </c:otherwise>--%>
<%--    </c:choose>--%>
<%--</div>--%>
