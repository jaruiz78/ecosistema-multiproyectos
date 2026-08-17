package com.corp.proyectobiotecnologia.application;

import com.corp.proyectobiotecnologia.application.service.BiotecnologiaApplicationService;
import com.corp.proyectobiotecnologia.domain.model.Biotecnologia;
import com.corp.proyectobiotecnologia.infrastructure.adapter.out.persistence.InMemoryBiotecnologiaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BiotecnologiaApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Biotecnologia usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryBiotecnologiaRepositoryAdapter repo = new InMemoryBiotecnologiaRepositoryAdapter();
        BiotecnologiaApplicationService service = new BiotecnologiaApplicationService(repo);

        Biotecnologia created = service.createBiotecnologia("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Biotecnologia> found = service.findBiotecnologiaById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Biotecnologia optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
