package com.corp.proyectomaritime.application;

import com.corp.proyectomaritime.application.service.MaritimeApplicationService;
import com.corp.proyectomaritime.domain.model.Maritime;
import com.corp.proyectomaritime.infrastructure.adapter.out.persistence.InMemoryMaritimeRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MaritimeApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Maritime usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryMaritimeRepositoryAdapter repo = new InMemoryMaritimeRepositoryAdapter();
        MaritimeApplicationService service = new MaritimeApplicationService(repo);

        Maritime created = service.createMaritime("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Maritime> found = service.findMaritimeById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Maritime optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
