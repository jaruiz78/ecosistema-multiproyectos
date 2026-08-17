package com.corp.proyectosubsurfacegeotwin.application;

import com.corp.proyectosubsurfacegeotwin.application.service.SubSurfaceGeoTwinApplicationService;
import com.corp.proyectosubsurfacegeotwin.domain.model.SubSurfaceGeoTwin;
import com.corp.proyectosubsurfacegeotwin.infrastructure.adapter.out.persistence.InMemorySubSurfaceGeoTwinRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SubSurfaceGeoTwinApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SubSurfaceGeoTwin usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySubSurfaceGeoTwinRepositoryAdapter repo = new InMemorySubSurfaceGeoTwinRepositoryAdapter();
        SubSurfaceGeoTwinApplicationService service = new SubSurfaceGeoTwinApplicationService(repo);

        SubSurfaceGeoTwin created = service.createSubSurfaceGeoTwin("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SubSurfaceGeoTwin> found = service.findSubSurfaceGeoTwinById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SubSurfaceGeoTwin optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
