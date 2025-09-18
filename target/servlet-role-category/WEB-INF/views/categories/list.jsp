<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Categories</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css"/></head>
<body>
<div class="container">
<div class="topbar">
    <h2>Categories</h2>
    <div>
        <a class="btn" href="${pageContext.request.contextPath}/categories/new">New Category</a>
        <a class="btn btn-danger" href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>
<table class="table">
    <thead><tr><th>ID</th><th>Name</th><th>Actions</th></tr></thead>
    <tbody>
    <c:forEach var="c" items="${categories}">
        <tr>
            <td>${c.id}</td>
            <td>${c.name}</td>
            <td>
                <a href="${pageContext.request.contextPath}/categories/${c.id}/edit">Edit</a>
                <form action="${pageContext.request.contextPath}/categories/${c.id}/delete" method="post" style="display:inline">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
</body>
</html>


