<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css"/>
</head>
<body>
<div class="container">
    <h2>Login</h2>
    <form class="card" action="${pageContext.request.contextPath}/login" method="post">
        <label>Username <input type="text" name="username" required></label>
        <label>Password <input type="password" name="password" required></label>
        <div class="actions">
            <button class="btn" type="submit">Login</button>
        </div>
        <div class="error">${error}</div>
    </form>
    
</div>
</body>
</html>


