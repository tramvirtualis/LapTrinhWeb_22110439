<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Category Form</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css"/></head>
<body>
<div class="container">
    <div class="topbar">
        <h2><c:choose><c:when test="${not empty category.id}">Edit Category</c:when><c:otherwise>New Category</c:otherwise></c:choose></h2>
        <div>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/categories">Back</a>
        </div>
    </div>
    <form class="card" action="${pageContext.request.contextPath}/categories<c:if test='${not empty category.id}'>/${category.id}</c:if>" method="post">
        <label>Name <input type="text" name="name" value="${category.name}" required></label>
        <div class="actions">
            <button class="btn" type="submit">Save</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/categories">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>


