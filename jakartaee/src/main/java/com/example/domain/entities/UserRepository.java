package com.example.domain.entities;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserRepository {

    @PersistenceContext(unitName = "DEV_PU1")
    private EntityManager em;

    public User find(String pk) {
        return em.find(User.class, pk);
    }

    public void persist(User entity) {
        em.persist(entity);
    }

    public User merge(User entity) {
        return em.merge(entity);
    }

    public void remove(User entity) {
        em.remove(entity);
    }

    public User findActive(String pk) {
        try {
            return em.createNamedQuery("User.findActive", User.class)
                    .setParameter("userId", pk)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
