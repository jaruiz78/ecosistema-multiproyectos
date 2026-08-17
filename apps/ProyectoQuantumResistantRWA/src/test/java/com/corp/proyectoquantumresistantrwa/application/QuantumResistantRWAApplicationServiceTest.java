package com.corp.proyectoquantumresistantrwa.application;

import com.corp.proyectoquantumresistantrwa.application.service.QuantumResistantRWAApplicationService;
import com.corp.proyectoquantumresistantrwa.domain.model.QuantumResistantRWA;
import com.corp.proyectoquantumresistantrwa.infrastructure.adapter.out.persistence.InMemoryQuantumResistantRWARepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class QuantumResistantRWAApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de QuantumResistantRWA usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryQuantumResistantRWARepositoryAdapter repo = new InMemoryQuantumResistantRWARepositoryAdapter();
        QuantumResistantRWAApplicationService service = new QuantumResistantRWAApplicationService(repo);

        QuantumResistantRWA created = service.createQuantumResistantRWA("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<QuantumResistantRWA> found = service.findQuantumResistantRWAById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        QuantumResistantRWA optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
