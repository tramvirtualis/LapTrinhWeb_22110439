package com.example.logincookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns= {"/hello","/xin-chao"})
public class HelloServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        String name="";
        Cookie[] cookie = req.getCookies();
        if (cookie != null) {
            for (Cookie c: cookie) {
                if(c.getName().equals("username")) {
                    name = c.getValue();
                }
            }
        }
        if(name.equals("")){
            resp.sendRedirect("LoginCookie.html");
        } else {
            //hiển thị lên trang bằng đối tượng PrintWriter()
            printWriter.println("Xin chao " + name);
        }
    }
}