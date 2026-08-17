package com.corp.proyectoheritagedigitaltwin3d.application;

import com.corp.proyectoheritagedigitaltwin3d.application.service.HeritageDigitalTwin3DApplicationService;
import com.corp.proyectoheritagedigitaltwin3d.domain.model.HeritageDigitalTwin3D;
import com.corp.proyectoheritagedigitaltwin3d.infrastructure.adapter.out.persistence.InMemoryHeritageDigitalTwin3DRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HeritageDigitalTwin3DApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de HeritageDigitalTwin3D usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryHeritageDigitalTwin3DRepositoryAdapter repo = new InMemoryHeritageDigitalTwin3DRepositoryAdapter();
        HeritageDigitalTwin3DApplicationService service = new HeritageDigitalTwin3DApplicationService(repo);

        HeritageDigitalTwin3D created = service.createHeritageDigitalTwin3D("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<HeritageDigitalTwin3D> found = service.findHeritageDigitalTwin3DById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        HeritageDigitalTwin3D optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
