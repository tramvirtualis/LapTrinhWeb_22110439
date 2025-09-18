package com.example.web.servlet;

import com.example.web.dao.ProfileDao;
import com.example.web.model.Profile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(urlPatterns = "/profile/*")
@MultipartConfig
public class ProfileServlet extends HttpServlet {
    private final ProfileDao profileDao = new ProfileDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile p = profileDao.getSingleton();
        if (p == null) { p = new Profile(); p.setFullname("Your Name"); p = profileDao.save(p); }
        req.setAttribute("profile", p);
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile p = profileDao.getSingleton();
        if (p == null) { p = new Profile(); }
        p.setFullname(req.getParameter("fullname"));
        p.setPhone(req.getParameter("phone"));

        Part filePart = req.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            String submitted = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String ext = submitted.contains(".") ? submitted.substring(submitted.lastIndexOf('.')) : "";
            String newName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
            Path staticUploads = Paths.get(req.getServletContext().getRealPath("/uploads"));
            Files.createDirectories(staticUploads);
            filePart.write(staticUploads.resolve(newName).toString());
            p.setImage(newName);
        }

        profileDao.save(p);
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}


