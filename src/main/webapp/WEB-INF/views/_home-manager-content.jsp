<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>Manager Home</h2>
<ul class="card">
    <c:forEach var="c" items="${categories}">
        <li>${c.name}</li>
    </c:forEach>
    
</ul>


