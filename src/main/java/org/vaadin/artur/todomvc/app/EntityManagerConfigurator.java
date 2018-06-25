package org.vaadin.artur.todomvc.app;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerConfigurator {

    @PersistenceContext
    private EntityManager entityManager;

    @Produces
    protected EntityManager createEntityManager() {
        return entityManager;
    }

}
