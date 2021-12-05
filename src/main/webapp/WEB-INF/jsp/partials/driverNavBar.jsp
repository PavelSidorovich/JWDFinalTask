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
                <a class="nav-link" href="${contextPath}/controller?command=incoming_order">Incoming orders</a>
            </li>
            <li class="nav-item">
<%--                todo my taxi--%>
                <a class="nav-link" href="${contextPath}/controller?command=my_bonuses">My taxi</a>
            </li>
        </ul>
        <div class="dropdown navbar-toggler-icon">
            <a class="nav-link" data-toggle="dropdown" href="#" role="button" aria-expanded="false"></a>
            <div class="dropdown-menu right-align">
                <a class="dropdown-item" href="#">Account control</a>
                <div class="dropdown-divider"></div>
                <c:if test="${not empty sessionScope.user}">
                    <a class="dropdown-item text-danger" href="${contextPath}/controller?command=logout">Logout</a>
                </c:if>
            </div>
        </div>
    </div>

</nav>