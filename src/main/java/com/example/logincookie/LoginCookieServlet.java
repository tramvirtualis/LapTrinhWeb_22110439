package com.example.logincookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns= {"/login"})
public class LoginCookieServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        resp.setContentType("text/html");
        String user = req.getParameter("username");
        String pass = req.getParameter("password");
        if(user.equals("tram") && pass.equals("123"))
        {
            Cookie cookie = new Cookie("username", user);
            cookie.setMaxAge(30);
            resp.addCookie(cookie);
            resp.sendRedirect("hello");
        }else {
            resp.sendRedirect("LoginCookie.html");
        }
    }
}
