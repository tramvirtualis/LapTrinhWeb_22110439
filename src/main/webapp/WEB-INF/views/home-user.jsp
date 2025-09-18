<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>User Home</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css"/></head>
<body>
<div class="container">
    <div class="topbar">
        <h2>User Home</h2>
        <div>
            <a class="btn" href="${pageContext.request.contextPath}/categories">Categories</a>
            <a class="btn btn-danger" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
    <ul class="card">
        <c:forEach var="c" items="${categories}">
            <li>${c.name}</li>
        </c:forEach>
    </ul>
    
</div>
</body>
</html>


