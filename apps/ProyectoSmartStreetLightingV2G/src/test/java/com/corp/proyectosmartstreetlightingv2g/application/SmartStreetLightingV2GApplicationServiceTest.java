package com.corp.proyectosmartstreetlightingv2g.application;

import com.corp.proyectosmartstreetlightingv2g.application.service.SmartStreetLightingV2GApplicationService;
import com.corp.proyectosmartstreetlightingv2g.domain.model.SmartStreetLightingV2G;
import com.corp.proyectosmartstreetlightingv2g.infrastructure.adapter.out.persistence.InMemorySmartStreetLightingV2GRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SmartStreetLightingV2GApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SmartStreetLightingV2G usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySmartStreetLightingV2GRepositoryAdapter repo = new InMemorySmartStreetLightingV2GRepositoryAdapter();
        SmartStreetLightingV2GApplicationService service = new SmartStreetLightingV2GApplicationService(repo);

        SmartStreetLightingV2G created = service.createSmartStreetLightingV2G("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SmartStreetLightingV2G> found = service.findSmartStreetLightingV2GById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SmartStreetLightingV2G optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
