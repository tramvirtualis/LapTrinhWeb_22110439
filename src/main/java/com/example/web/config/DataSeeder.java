package com.example.web.config;

import com.example.web.dao.CategoryDao;
import com.example.web.dao.UserDao;
import com.example.web.model.Category;
import com.example.web.model.User;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DataSeeder implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserDao userDao = new UserDao();
        if (userDao.findByUsername("admin") == null) {
            User user = new User(); user.setUsername("user"); user.setPassword("user"); user.setRoleId(1); userDao.save(user);
            User manager = new User(); manager.setUsername("manager"); manager.setPassword("manager"); manager.setRoleId(2); userDao.save(manager);
            User admin = new User(); admin.setUsername("admin"); admin.setPassword("admin"); admin.setRoleId(3); userDao.save(admin);

            CategoryDao categoryDao = new CategoryDao();
            Category c1 = new Category(); c1.setName("General"); c1.setOwner(admin); categoryDao.save(c1);
            Category c2 = new Category(); c2.setName("Manager Stuff"); c2.setOwner(manager); categoryDao.save(c2);
        }
    }
}


