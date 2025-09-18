package com.example.web.servlet;

import com.example.web.dao.CategoryDao;
import com.example.web.dao.UserDao;
import com.example.web.model.Category;
import com.example.web.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/categories/*")
public class CategoryServlet extends HttpServlet {
    private final CategoryDao categoryDao = new CategoryDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            User user = (User) req.getSession().getAttribute("user");
            req.setAttribute("categories", user.getRoleId() == 3 ? categoryDao.findAll() : categoryDao.findByOwner(user));
            req.getRequestDispatcher("/WEB-INF/views/categories/list.jsp").forward(req, resp);
            return;
        }
        if (path.equals("/new") || path.matches("/\\d+/edit")) {
            if (path.matches("/\\d+/edit")) {
                Long id = Long.valueOf(path.split("/")[1]);
                Category c = categoryDao.findById(id);
                if (c == null) { resp.sendRedirect(req.getContextPath() + "/categories"); return; }
                req.setAttribute("category", c);
            }
            req.getRequestDispatcher("/WEB-INF/views/categories/form.jsp").forward(req, resp);
        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        User user = (User) req.getSession().getAttribute("user");
        if (path == null || "/".equals(path)) {
            Category c = new Category();
            c.setName(req.getParameter("name"));
            c.setOwner(user);
            categoryDao.save(c);
            resp.sendRedirect(req.getContextPath() + "/categories");
            return;
        }
        if (path.matches("/\\d+")) {
            Long id = Long.valueOf(path.substring(1));
            Category c = categoryDao.findById(id);
            if (c != null && (user.getRoleId() == 3 || c.getOwner().getId().equals(user.getId()))) {
                c.setName(req.getParameter("name"));
                categoryDao.save(c);
            }
            resp.sendRedirect(req.getContextPath() + "/categories");
            return;
        }
        if (path.matches("/\\d+/delete")) {
            Long id = Long.valueOf(path.split("/")[1]);
            Category c = categoryDao.findById(id);
            if (c != null && (user.getRoleId() == 3 || c.getOwner().getId().equals(user.getId()))) {
                categoryDao.delete(c);
            }
            resp.sendRedirect(req.getContextPath() + "/categories");
            return;
        }
        resp.sendError(404);
    }
}


