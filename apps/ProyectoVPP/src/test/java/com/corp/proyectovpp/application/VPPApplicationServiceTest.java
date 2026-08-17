package com.corp.proyectovpp.application;

import com.corp.proyectovpp.application.service.VPPApplicationService;
import com.corp.proyectovpp.domain.model.VPP;
import com.corp.proyectovpp.infrastructure.adapter.out.persistence.InMemoryVPPRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class VPPApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de VPP usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryVPPRepositoryAdapter repo = new InMemoryVPPRepositoryAdapter();
        VPPApplicationService service = new VPPApplicationService(repo);

        VPP created = service.createVPP("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<VPP> found = service.findVPPById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        VPP optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
