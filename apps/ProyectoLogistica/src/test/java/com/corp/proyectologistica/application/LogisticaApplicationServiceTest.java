package com.corp.proyectologistica.application;

import com.corp.proyectologistica.application.service.LogisticaApplicationService;
import com.corp.proyectologistica.domain.model.Logistica;
import com.corp.proyectologistica.infrastructure.adapter.out.persistence.InMemoryLogisticaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LogisticaApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Logistica usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryLogisticaRepositoryAdapter repo = new InMemoryLogisticaRepositoryAdapter();
        LogisticaApplicationService service = new LogisticaApplicationService(repo);

        Logistica created = service.createLogistica("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Logistica> found = service.findLogisticaById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Logistica optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
