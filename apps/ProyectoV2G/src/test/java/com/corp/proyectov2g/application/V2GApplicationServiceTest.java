package com.corp.proyectov2g.application;

import com.corp.proyectov2g.application.service.V2GApplicationService;
import com.corp.proyectov2g.domain.model.V2G;
import com.corp.proyectov2g.infrastructure.adapter.out.persistence.InMemoryV2GRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class V2GApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de V2G usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryV2GRepositoryAdapter repo = new InMemoryV2GRepositoryAdapter();
        V2GApplicationService service = new V2GApplicationService(repo);

        V2G created = service.createV2G("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<V2G> found = service.findV2GById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        V2G optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
