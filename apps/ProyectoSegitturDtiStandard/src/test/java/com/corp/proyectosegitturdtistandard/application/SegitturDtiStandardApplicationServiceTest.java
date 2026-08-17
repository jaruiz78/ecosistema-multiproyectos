package com.corp.proyectosegitturdtistandard.application;

import com.corp.proyectosegitturdtistandard.application.service.SegitturDtiStandardApplicationService;
import com.corp.proyectosegitturdtistandard.domain.model.SegitturDtiStandard;
import com.corp.proyectosegitturdtistandard.infrastructure.adapter.out.persistence.InMemorySegitturDtiStandardRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SegitturDtiStandardApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SegitturDtiStandard usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySegitturDtiStandardRepositoryAdapter repo = new InMemorySegitturDtiStandardRepositoryAdapter();
        SegitturDtiStandardApplicationService service = new SegitturDtiStandardApplicationService(repo);

        SegitturDtiStandard created = service.createSegitturDtiStandard("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SegitturDtiStandard> found = service.findSegitturDtiStandardById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SegitturDtiStandard optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
