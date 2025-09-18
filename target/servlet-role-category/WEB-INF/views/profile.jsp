<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Profile</title>
</head>
<body>
<div id="content" class="container py-4">
    <div class="row">
        <div class="col-md-6">
            <h3>Update Profile</h3>
            <form class="card p-3" action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label class="form-label">Fullname</label>
                    <input type="text" name="fullname" class="form-control" value="${profile.fullname}" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" class="form-control" value="${profile.phone}" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Image</label>
                    <input type="file" name="imageFile" class="form-control" />
                </div>
                <button type="submit" class="btn btn-primary">Update Profile</button>
            </form>
        </div>
        <div class="col-md-6">
            <h3>Preview</h3>
            <div class="card p-3">
                <p><strong>Name:</strong> ${profile.fullname}</p>
                <p><strong>Phone:</strong> ${profile.phone}</p>
                <c:if test="${not empty profile.image}">
                    <img src="${pageContext.request.contextPath}/uploads/${profile.image}" class="img-fluid rounded border" alt="Profile image" />
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>


