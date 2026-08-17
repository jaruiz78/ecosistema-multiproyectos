package com.corp.proyectodroneairspaceuspace.application;

import com.corp.proyectodroneairspaceuspace.application.service.DroneAirspaceUSpaceApplicationService;
import com.corp.proyectodroneairspaceuspace.domain.model.DroneAirspaceUSpace;
import com.corp.proyectodroneairspaceuspace.infrastructure.adapter.out.persistence.InMemoryDroneAirspaceUSpaceRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DroneAirspaceUSpaceApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de DroneAirspaceUSpace usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryDroneAirspaceUSpaceRepositoryAdapter repo = new InMemoryDroneAirspaceUSpaceRepositoryAdapter();
        DroneAirspaceUSpaceApplicationService service = new DroneAirspaceUSpaceApplicationService(repo);

        DroneAirspaceUSpace created = service.createDroneAirspaceUSpace("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<DroneAirspaceUSpace> found = service.findDroneAirspaceUSpaceById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        DroneAirspaceUSpace optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
