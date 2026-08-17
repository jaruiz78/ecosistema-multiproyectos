package com.corp.proyectoquantumsatellitesync.application;

import com.corp.proyectoquantumsatellitesync.application.service.QuantumSatelliteSyncApplicationService;
import com.corp.proyectoquantumsatellitesync.domain.model.QuantumSatelliteSync;
import com.corp.proyectoquantumsatellitesync.infrastructure.adapter.out.persistence.InMemoryQuantumSatelliteSyncRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class QuantumSatelliteSyncApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de QuantumSatelliteSync usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryQuantumSatelliteSyncRepositoryAdapter repo = new InMemoryQuantumSatelliteSyncRepositoryAdapter();
        QuantumSatelliteSyncApplicationService service = new QuantumSatelliteSyncApplicationService(repo);

        QuantumSatelliteSync created = service.createQuantumSatelliteSync("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<QuantumSatelliteSync> found = service.findQuantumSatelliteSyncById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        QuantumSatelliteSync optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
