package com.corp.proyectosoilbiocarbontwin.application;

import com.corp.proyectosoilbiocarbontwin.application.service.SoilBioCarbonTwinApplicationService;
import com.corp.proyectosoilbiocarbontwin.domain.model.SoilBioCarbonTwin;
import com.corp.proyectosoilbiocarbontwin.infrastructure.adapter.out.persistence.InMemorySoilBioCarbonTwinRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SoilBioCarbonTwinApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SoilBioCarbonTwin usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySoilBioCarbonTwinRepositoryAdapter repo = new InMemorySoilBioCarbonTwinRepositoryAdapter();
        SoilBioCarbonTwinApplicationService service = new SoilBioCarbonTwinApplicationService(repo);

        SoilBioCarbonTwin created = service.createSoilBioCarbonTwin("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SoilBioCarbonTwin> found = service.findSoilBioCarbonTwinById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SoilBioCarbonTwin optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
