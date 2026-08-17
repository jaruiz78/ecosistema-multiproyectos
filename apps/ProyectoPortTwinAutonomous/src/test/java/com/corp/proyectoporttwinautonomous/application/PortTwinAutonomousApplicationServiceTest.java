package com.corp.proyectoporttwinautonomous.application;

import com.corp.proyectoporttwinautonomous.application.service.PortTwinAutonomousApplicationService;
import com.corp.proyectoporttwinautonomous.domain.model.PortTwinAutonomous;
import com.corp.proyectoporttwinautonomous.infrastructure.adapter.out.persistence.InMemoryPortTwinAutonomousRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PortTwinAutonomousApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de PortTwinAutonomous usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryPortTwinAutonomousRepositoryAdapter repo = new InMemoryPortTwinAutonomousRepositoryAdapter();
        PortTwinAutonomousApplicationService service = new PortTwinAutonomousApplicationService(repo);

        PortTwinAutonomous created = service.createPortTwinAutonomous("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<PortTwinAutonomous> found = service.findPortTwinAutonomousById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        PortTwinAutonomous optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
