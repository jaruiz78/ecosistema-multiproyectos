package com.corp.proyectosmartwaterdesal.application;

import com.corp.proyectosmartwaterdesal.application.service.SmartWaterDesalApplicationService;
import com.corp.proyectosmartwaterdesal.domain.model.SmartWaterDesal;
import com.corp.proyectosmartwaterdesal.infrastructure.adapter.out.persistence.InMemorySmartWaterDesalRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SmartWaterDesalApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SmartWaterDesal usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySmartWaterDesalRepositoryAdapter repo = new InMemorySmartWaterDesalRepositoryAdapter();
        SmartWaterDesalApplicationService service = new SmartWaterDesalApplicationService(repo);

        SmartWaterDesal created = service.createSmartWaterDesal("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SmartWaterDesal> found = service.findSmartWaterDesalById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SmartWaterDesal optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
