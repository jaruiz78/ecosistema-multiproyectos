package com.corp.proyectob2g.application;

import com.corp.proyectob2g.application.service.B2GApplicationService;
import com.corp.proyectob2g.domain.model.B2G;
import com.corp.proyectob2g.infrastructure.adapter.out.persistence.InMemoryB2GRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class B2GApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de B2G usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryB2GRepositoryAdapter repo = new InMemoryB2GRepositoryAdapter();
        B2GApplicationService service = new B2GApplicationService(repo);

        B2G created = service.createB2G("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<B2G> found = service.findB2GById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        B2G optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
