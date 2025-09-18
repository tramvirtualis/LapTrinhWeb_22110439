package com.example.web.security;

import com.example.web.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.equals("/login") || path.equals("/login.jsp") || path.equals("/logout") || path.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        int role = user.getRoleId();

        boolean allowed = switch (role) {
            case 1 -> path.startsWith("/user/") || path.startsWith("/categories");
            case 2 -> path.startsWith("/manager/") || path.startsWith("/categories");
            case 3 -> path.startsWith("/admin/") || path.startsWith("/categories") || path.startsWith("/user/") || path.startsWith("/manager/");
            default -> false;
        };

        if (!allowed) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}


