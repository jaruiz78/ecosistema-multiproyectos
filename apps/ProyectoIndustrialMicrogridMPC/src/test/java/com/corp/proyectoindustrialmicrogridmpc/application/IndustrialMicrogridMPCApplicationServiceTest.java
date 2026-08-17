package com.corp.proyectoindustrialmicrogridmpc.application;

import com.corp.proyectoindustrialmicrogridmpc.application.service.IndustrialMicrogridMPCApplicationService;
import com.corp.proyectoindustrialmicrogridmpc.domain.model.IndustrialMicrogridMPC;
import com.corp.proyectoindustrialmicrogridmpc.infrastructure.adapter.out.persistence.InMemoryIndustrialMicrogridMPCRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class IndustrialMicrogridMPCApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de IndustrialMicrogridMPC usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryIndustrialMicrogridMPCRepositoryAdapter repo = new InMemoryIndustrialMicrogridMPCRepositoryAdapter();
        IndustrialMicrogridMPCApplicationService service = new IndustrialMicrogridMPCApplicationService(repo);

        IndustrialMicrogridMPC created = service.createIndustrialMicrogridMPC("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<IndustrialMicrogridMPC> found = service.findIndustrialMicrogridMPCById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        IndustrialMicrogridMPC optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
