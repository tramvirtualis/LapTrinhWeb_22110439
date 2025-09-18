package com.example.web.servlet;

import com.example.web.dao.UserDao;
import com.example.web.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class AuthServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/logout".equals(servletPath)) {
            if (req.getSession(false) != null) {
                req.getSession(false).invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userDao.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("error", "Invalid credentials");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        switch (user.getRoleId()) {
            case 1 -> resp.sendRedirect(req.getContextPath() + "/user/home");
            case 2 -> resp.sendRedirect(req.getContextPath() + "/manager/home");
            case 3 -> resp.sendRedirect(req.getContextPath() + "/admin/home");
            default -> resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }
}


