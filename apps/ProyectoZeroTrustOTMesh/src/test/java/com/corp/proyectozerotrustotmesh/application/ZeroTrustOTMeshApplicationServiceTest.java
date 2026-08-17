package com.corp.proyectozerotrustotmesh.application;

import com.corp.proyectozerotrustotmesh.application.service.ZeroTrustOTMeshApplicationService;
import com.corp.proyectozerotrustotmesh.domain.model.ZeroTrustOTMesh;
import com.corp.proyectozerotrustotmesh.infrastructure.adapter.out.persistence.InMemoryZeroTrustOTMeshRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ZeroTrustOTMeshApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de ZeroTrustOTMesh usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryZeroTrustOTMeshRepositoryAdapter repo = new InMemoryZeroTrustOTMeshRepositoryAdapter();
        ZeroTrustOTMeshApplicationService service = new ZeroTrustOTMeshApplicationService(repo);

        ZeroTrustOTMesh created = service.createZeroTrustOTMesh("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<ZeroTrustOTMesh> found = service.findZeroTrustOTMeshById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        ZeroTrustOTMesh optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
