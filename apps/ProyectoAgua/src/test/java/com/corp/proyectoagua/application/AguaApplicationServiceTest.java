package com.corp.proyectoagua.application;

import com.corp.proyectoagua.application.service.AguaApplicationService;
import com.corp.proyectoagua.domain.model.Agua;
import com.corp.proyectoagua.infrastructure.adapter.out.persistence.InMemoryAguaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AguaApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Agua usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryAguaRepositoryAdapter repo = new InMemoryAguaRepositoryAdapter();
        AguaApplicationService service = new AguaApplicationService(repo);

        Agua created = service.createAgua("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Agua> found = service.findAguaById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Agua optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
