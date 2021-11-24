<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow">
    <h5 class="my-0 mr-md-auto font-weight-normal">Buber Taxi</h5>
    <nav class="my-2 my-md-0 mr-md-3">
        <a class="p-2 text-dark" href="#">Become a driver</a>
    </nav>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <a class="btn btn-outline-primary" href="${contextPath}/controller?command=logout">Logout</a>
        </c:when>
        <c:otherwise>
            <a class="btn btn-outline-primary" href="${contextPath}/controller?command=show_login">Sign in</a>
        </c:otherwise>
    </c:choose>
</div>
