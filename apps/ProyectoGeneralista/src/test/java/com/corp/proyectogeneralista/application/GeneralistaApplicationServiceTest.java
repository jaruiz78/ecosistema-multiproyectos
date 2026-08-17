package com.corp.proyectogeneralista.application;

import com.corp.proyectogeneralista.application.service.GeneralistaApplicationService;
import com.corp.proyectogeneralista.domain.model.Generalista;
import com.corp.proyectogeneralista.infrastructure.adapter.out.persistence.InMemoryGeneralistaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GeneralistaApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Generalista usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryGeneralistaRepositoryAdapter repo = new InMemoryGeneralistaRepositoryAdapter();
        GeneralistaApplicationService service = new GeneralistaApplicationService(repo);

        Generalista created = service.createGeneralista("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Generalista> found = service.findGeneralistaById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Generalista optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
