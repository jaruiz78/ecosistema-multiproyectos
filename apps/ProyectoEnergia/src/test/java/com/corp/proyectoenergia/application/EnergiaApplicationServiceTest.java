package com.corp.proyectoenergia.application;

import com.corp.proyectoenergia.application.service.EnergiaApplicationService;
import com.corp.proyectoenergia.domain.model.Energia;
import com.corp.proyectoenergia.infrastructure.adapter.out.persistence.InMemoryEnergiaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EnergiaApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Energia usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryEnergiaRepositoryAdapter repo = new InMemoryEnergiaRepositoryAdapter();
        EnergiaApplicationService service = new EnergiaApplicationService(repo);

        Energia created = service.createEnergia("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Energia> found = service.findEnergiaById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Energia optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
