package com.ps.recipes.service;

import com.ps.recipes.domain.Recipe;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BuildIndexOnStartup {

    private final SearchSession searchSession;

    @Autowired
    public BuildIndexOnStartup(EntityManager entityManager) {
        this.searchSession = Search.session(entityManager);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void buildIndexAfterStartup() {
        try {
            searchSession.massIndexer().startAndWait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Mass indexing interrupted", e);
        }
    }
}

