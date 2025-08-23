package com.example.logincookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns= {"/LoginSession"})
public class LoginSessionServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username.equals("tram")&& password.equals("123")) {
            out.print("Chào mừng bạn, " + username);
            HttpSession session = request.getSession();
            session.setAttribute("name", username);
        } else {
            out.print("Tài khoản hoặc mật khẩu không chính xác");
            request.getRequestDispatcher("LoginSession.html").include(request, response);
        }
    }
}
