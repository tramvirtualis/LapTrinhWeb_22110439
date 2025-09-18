package com.example.web.dao;

import com.example.web.jpa.JpaUtil;
import com.example.web.model.Profile;
import jakarta.persistence.EntityManager;

public class ProfileDao {
    public Profile getSingleton() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Profile p", Profile.class).setMaxResults(1).getResultList().stream().findFirst().orElse(null);
        } finally { em.close(); }
    }

    public Profile save(Profile p) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (p.getId() == null) em.persist(p); else p = em.merge(p);
            em.getTransaction().commit();
            return p;
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }
}


