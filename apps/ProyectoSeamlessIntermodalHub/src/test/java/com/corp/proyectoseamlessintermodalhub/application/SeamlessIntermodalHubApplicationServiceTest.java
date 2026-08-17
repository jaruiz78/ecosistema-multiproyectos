package com.corp.proyectoseamlessintermodalhub.application;

import com.corp.proyectoseamlessintermodalhub.application.service.SeamlessIntermodalHubApplicationService;
import com.corp.proyectoseamlessintermodalhub.domain.model.SeamlessIntermodalHub;
import com.corp.proyectoseamlessintermodalhub.infrastructure.adapter.out.persistence.InMemorySeamlessIntermodalHubRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SeamlessIntermodalHubApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SeamlessIntermodalHub usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySeamlessIntermodalHubRepositoryAdapter repo = new InMemorySeamlessIntermodalHubRepositoryAdapter();
        SeamlessIntermodalHubApplicationService service = new SeamlessIntermodalHubApplicationService(repo);

        SeamlessIntermodalHub created = service.createSeamlessIntermodalHub("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SeamlessIntermodalHub> found = service.findSeamlessIntermodalHubById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SeamlessIntermodalHub optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
