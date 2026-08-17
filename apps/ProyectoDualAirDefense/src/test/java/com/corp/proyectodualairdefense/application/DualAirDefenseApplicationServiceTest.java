package com.corp.proyectodualairdefense.application;

import com.corp.proyectodualairdefense.application.service.DualAirDefenseApplicationService;
import com.corp.proyectodualairdefense.domain.model.DualAirDefense;
import com.corp.proyectodualairdefense.infrastructure.adapter.out.persistence.InMemoryDualAirDefenseRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DualAirDefenseApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de DualAirDefense usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryDualAirDefenseRepositoryAdapter repo = new InMemoryDualAirDefenseRepositoryAdapter();
        DualAirDefenseApplicationService service = new DualAirDefenseApplicationService(repo);

        DualAirDefense created = service.createDualAirDefense("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<DualAirDefense> found = service.findDualAirDefenseById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        DualAirDefense optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
