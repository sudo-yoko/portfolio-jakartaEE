package com.example.development;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerProvider {

    public static enum PersistenceUnitName {
        DEV_PU1("TEST_PU1"),
        DEV_PU2("TEST_PU2");

        private final String value;

        PersistenceUnitName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final Map<PersistenceUnitName, PersistenceUnitContext> units = new HashMap<>();

    public EntityManagerProvider() {
        for (PersistenceUnitName unitName : PersistenceUnitName.values()) {
            units.put(unitName, new PersistenceUnitContext(unitName));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public EntityManager getEntityManager(PersistenceUnitName unitName) {
        return units.get(unitName).getEntityManager();
    }

    public void close() {
        units.forEach((unitName, context) -> {
            context.close();
        });
    }

    /**
     * 永続化ユニット名と、それに紐づくEntityManagerインスタンスを保持するクラス
     */
    public static class PersistenceUnitContext {
        private final PersistenceUnitName unitName;
        private final EntityManagerFactory emf;
        private final EntityManager em;

        public PersistenceUnitContext(PersistenceUnitName unitName) {
            this.unitName = unitName;
            this.emf = Persistence.createEntityManagerFactory(unitName.getValue());
            this.em = emf.createEntityManager();
        }

        public PersistenceUnitName getUnitName() {
            return unitName;
        }

        public EntityManager getEntityManager() {
            return em;
        }

        public void close() {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}
