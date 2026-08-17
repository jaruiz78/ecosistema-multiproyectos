package com.corp.proyectosalud.application;

import com.corp.proyectosalud.application.service.SaludApplicationService;
import com.corp.proyectosalud.domain.model.Salud;
import com.corp.proyectosalud.infrastructure.adapter.out.persistence.InMemorySaludRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SaludApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Salud usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySaludRepositoryAdapter repo = new InMemorySaludRepositoryAdapter();
        SaludApplicationService service = new SaludApplicationService(repo);

        Salud created = service.createSalud("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Salud> found = service.findSaludById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Salud optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
