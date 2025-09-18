<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${param.pageTitle != null ? param.pageTitle : 'App'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">RoleCategory</a>
        <div class="ms-auto">
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/categories">Categories</a>
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/profile">Profile</a>
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <div class="row">
        <aside class="col-12 col-md-3 col-xl-2 p-3 border-end bg-white">
            <div class="list-group list-group-flush">
                <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/user/home">User Home</a>
                <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/manager/home">Manager Home</a>
                <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/admin/home">Admin Home</a>
            </div>
        </aside>
        <main class="col p-4">
            <jsp:include page="${param.contentPage}"/>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


