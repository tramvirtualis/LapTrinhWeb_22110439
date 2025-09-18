package com.example.web.servlet;

import com.example.web.dao.CategoryDao;
import com.example.web.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/user/home", "/manager/home", "/admin/home"})
public class HomeServlet extends HttpServlet {
    private final CategoryDao categoryDao = new CategoryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (path.startsWith("/manager")) {
            User user = (User) req.getSession().getAttribute("user");
            req.setAttribute("categories", categoryDao.findByOwner(user));
            req.getRequestDispatcher("/WEB-INF/views/home-manager.jsp").forward(req, resp);
        } else {
            req.setAttribute("categories", categoryDao.findAll());
            if (path.startsWith("/admin")) {
                req.getRequestDispatcher("/WEB-INF/views/home-admin.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/WEB-INF/views/home-user.jsp").forward(req, resp);
            }
        }
    }
}


