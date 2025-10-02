package com.example.category.config;

import com.example.category.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // Allow access to public pages and static resources
        if (requestURI.equals("/login") || requestURI.equals("/register") || 
            requestURI.startsWith("/css/") || requestURI.startsWith("/js/") || 
            requestURI.startsWith("/images/") || requestURI.startsWith("/static/")) {
            return true;
        }
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        // Get user from session
        User user = (User) session.getAttribute("user");
        User.Role userRole = user.getRole();
        
        // Role-based access control
        if (requestURI.startsWith("/admin")) {
            if (userRole != User.Role.ADMIN) {
                response.sendRedirect("/login");
                return false;
            }
        } else if (requestURI.startsWith("/user")) {
            if (userRole != User.Role.CUSTOMER) {
                response.sendRedirect("/login");
                return false;
            }
        } else if (requestURI.startsWith("/games/new") || 
                   requestURI.matches("/games/\\d+/edit") || 
                   requestURI.matches("/games/\\d+/delete")) {
            // Admin-only game management operations
            if (userRole != User.Role.ADMIN) {
                response.sendRedirect("/user");
                return false;
            }
        }
        
        return true;
    }
}

