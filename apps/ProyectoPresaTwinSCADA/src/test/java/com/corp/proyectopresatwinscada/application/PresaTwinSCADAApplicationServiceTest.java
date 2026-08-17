package com.corp.proyectopresatwinscada.application;

import com.corp.proyectopresatwinscada.application.service.PresaTwinSCADAApplicationService;
import com.corp.proyectopresatwinscada.domain.model.PresaTwinSCADA;
import com.corp.proyectopresatwinscada.infrastructure.adapter.out.persistence.InMemoryPresaTwinSCADARepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PresaTwinSCADAApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de PresaTwinSCADA usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryPresaTwinSCADARepositoryAdapter repo = new InMemoryPresaTwinSCADARepositoryAdapter();
        PresaTwinSCADAApplicationService service = new PresaTwinSCADAApplicationService(repo);

        PresaTwinSCADA created = service.createPresaTwinSCADA("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<PresaTwinSCADA> found = service.findPresaTwinSCADAById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        PresaTwinSCADA optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
