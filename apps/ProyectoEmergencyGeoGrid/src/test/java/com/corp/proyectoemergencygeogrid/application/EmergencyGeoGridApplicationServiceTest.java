package com.corp.proyectoemergencygeogrid.application;

import com.corp.proyectoemergencygeogrid.application.service.EmergencyGeoGridApplicationService;
import com.corp.proyectoemergencygeogrid.domain.model.EmergencyGeoGrid;
import com.corp.proyectoemergencygeogrid.infrastructure.adapter.out.persistence.InMemoryEmergencyGeoGridRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EmergencyGeoGridApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de EmergencyGeoGrid usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryEmergencyGeoGridRepositoryAdapter repo = new InMemoryEmergencyGeoGridRepositoryAdapter();
        EmergencyGeoGridApplicationService service = new EmergencyGeoGridApplicationService(repo);

        EmergencyGeoGrid created = service.createEmergencyGeoGrid("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<EmergencyGeoGrid> found = service.findEmergencyGeoGridById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        EmergencyGeoGrid optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
