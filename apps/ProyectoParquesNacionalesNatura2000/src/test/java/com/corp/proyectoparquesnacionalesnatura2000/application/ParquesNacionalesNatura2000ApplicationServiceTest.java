package com.corp.proyectoparquesnacionalesnatura2000.application;

import com.corp.proyectoparquesnacionalesnatura2000.application.service.ParquesNacionalesNatura2000ApplicationService;
import com.corp.proyectoparquesnacionalesnatura2000.domain.model.ParquesNacionalesNatura2000;
import com.corp.proyectoparquesnacionalesnatura2000.infrastructure.adapter.out.persistence.InMemoryParquesNacionalesNatura2000RepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ParquesNacionalesNatura2000ApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de ParquesNacionalesNatura2000 usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryParquesNacionalesNatura2000RepositoryAdapter repo = new InMemoryParquesNacionalesNatura2000RepositoryAdapter();
        ParquesNacionalesNatura2000ApplicationService service = new ParquesNacionalesNatura2000ApplicationService(repo);

        ParquesNacionalesNatura2000 created = service.createParquesNacionalesNatura2000("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<ParquesNacionalesNatura2000> found = service.findParquesNacionalesNatura2000ById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        ParquesNacionalesNatura2000 optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
