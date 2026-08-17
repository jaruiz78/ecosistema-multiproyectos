package com.corp.proyectocircular.application;

import com.corp.proyectocircular.application.service.CircularApplicationService;
import com.corp.proyectocircular.domain.model.Circular;
import com.corp.proyectocircular.infrastructure.adapter.out.persistence.InMemoryCircularRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CircularApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Circular usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCircularRepositoryAdapter repo = new InMemoryCircularRepositoryAdapter();
        CircularApplicationService service = new CircularApplicationService(repo);

        Circular created = service.createCircular("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Circular> found = service.findCircularById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Circular optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
