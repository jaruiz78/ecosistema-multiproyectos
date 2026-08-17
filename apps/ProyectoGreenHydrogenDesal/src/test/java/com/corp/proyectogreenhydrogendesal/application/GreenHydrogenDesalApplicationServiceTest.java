package com.corp.proyectogreenhydrogendesal.application;

import com.corp.proyectogreenhydrogendesal.application.service.GreenHydrogenDesalApplicationService;
import com.corp.proyectogreenhydrogendesal.domain.model.GreenHydrogenDesal;
import com.corp.proyectogreenhydrogendesal.infrastructure.adapter.out.persistence.InMemoryGreenHydrogenDesalRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GreenHydrogenDesalApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de GreenHydrogenDesal usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryGreenHydrogenDesalRepositoryAdapter repo = new InMemoryGreenHydrogenDesalRepositoryAdapter();
        GreenHydrogenDesalApplicationService service = new GreenHydrogenDesalApplicationService(repo);

        GreenHydrogenDesal created = service.createGreenHydrogenDesal("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<GreenHydrogenDesal> found = service.findGreenHydrogenDesalById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        GreenHydrogenDesal optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
