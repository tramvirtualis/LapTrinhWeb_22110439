package com.example.web.dao;

import com.example.web.jpa.JpaUtil;
import com.example.web.model.Category;
import com.example.web.model.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoryDao {
    public List<Category> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Category> findByOwner(User owner) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c WHERE c.owner = :o", Category.class)
                    .setParameter("o", owner)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Category c) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (c.getId() == null) {
                em.persist(c);
            } else {
                em.merge(c);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }

    public void delete(Category c) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Category managed = em.merge(c);
            em.remove(managed);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }
}


