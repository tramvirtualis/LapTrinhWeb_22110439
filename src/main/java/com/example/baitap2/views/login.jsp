<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <style>
        body { font-family: Arial, sans-serif; padding: 24px; }
        .container { max-width: 420px; margin: 0 auto; }
        .field { margin-bottom: 12px; display: flex; flex-direction: column; }
        label { margin-bottom: 4px; font-weight: 600; }
        input[type="text"], input[type="password"] { padding: 10px; font-size: 14px; }
        .actions { margin-top: 16px; display: flex; align-items: center; gap: 12px; }
        .alert { color: #b00020; margin-bottom: 12px; }
        button { padding: 10px 16px; }
    </style>
    </head>
<body>
<div class="container">
    <h2>Đăng nhập</h2>
    <% if (request.getAttribute("alert") != null) { %>
        <div class="alert"><%= request.getAttribute("alert") %></div>
    <% } %>
    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="field">
            <label for="username">Tài khoản</label>
            <input id="username" type="text" name="username" placeholder="Nhập tài khoản" />
        </div>
        <div class="field">
            <label for="password">Mật khẩu</label>
            <input id="password" type="password" name="password" placeholder="Nhập mật khẩu" />
        </div>
        <div class="actions">
            <label>
                <input type="checkbox" name="remember" /> Ghi nhớ đăng nhập
            </label>
            <button type="submit">Đăng nhập</button>
        </div>
    </form>
    <div style="margin-top: 16px;">
        <a href="<%= request.getContextPath() %>/">Về trang chủ</a>
    </div>
 </div>
</body>
</html>
