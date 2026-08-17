package com.corp.proyectoturismotermalbalnearios.application;

import com.corp.proyectoturismotermalbalnearios.application.service.TurismoTermalBalneariosApplicationService;
import com.corp.proyectoturismotermalbalnearios.domain.model.TurismoTermalBalnearios;
import com.corp.proyectoturismotermalbalnearios.infrastructure.adapter.out.persistence.InMemoryTurismoTermalBalneariosRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TurismoTermalBalneariosApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de TurismoTermalBalnearios usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryTurismoTermalBalneariosRepositoryAdapter repo = new InMemoryTurismoTermalBalneariosRepositoryAdapter();
        TurismoTermalBalneariosApplicationService service = new TurismoTermalBalneariosApplicationService(repo);

        TurismoTermalBalnearios created = service.createTurismoTermalBalnearios("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<TurismoTermalBalnearios> found = service.findTurismoTermalBalneariosById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        TurismoTermalBalnearios optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
