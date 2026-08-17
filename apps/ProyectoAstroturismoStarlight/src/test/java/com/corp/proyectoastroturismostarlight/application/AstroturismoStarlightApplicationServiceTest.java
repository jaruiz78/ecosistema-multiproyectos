package com.corp.proyectoastroturismostarlight.application;

import com.corp.proyectoastroturismostarlight.application.service.AstroturismoStarlightApplicationService;
import com.corp.proyectoastroturismostarlight.domain.model.AstroturismoStarlight;
import com.corp.proyectoastroturismostarlight.infrastructure.adapter.out.persistence.InMemoryAstroturismoStarlightRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AstroturismoStarlightApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de AstroturismoStarlight usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryAstroturismoStarlightRepositoryAdapter repo = new InMemoryAstroturismoStarlightRepositoryAdapter();
        AstroturismoStarlightApplicationService service = new AstroturismoStarlightApplicationService(repo);

        AstroturismoStarlight created = service.createAstroturismoStarlight("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<AstroturismoStarlight> found = service.findAstroturismoStarlightById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        AstroturismoStarlight optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
