<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.partials.commonFooter" var="loc"/>
<fmt:message bundle="${loc}" key="label.copyright" var="copyrightLabel"/>

<div class="jumbotron text-center" style="margin-bottom:0">
    <p>${copyrightLabel}</p>
</div>

