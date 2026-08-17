package com.corp.proyectocaminosantiagoxacobeo.application;

import com.corp.proyectocaminosantiagoxacobeo.application.service.CaminoSantiagoXacobeoApplicationService;
import com.corp.proyectocaminosantiagoxacobeo.domain.model.CaminoSantiagoXacobeo;
import com.corp.proyectocaminosantiagoxacobeo.infrastructure.adapter.out.persistence.InMemoryCaminoSantiagoXacobeoRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CaminoSantiagoXacobeoApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de CaminoSantiagoXacobeo usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCaminoSantiagoXacobeoRepositoryAdapter repo = new InMemoryCaminoSantiagoXacobeoRepositoryAdapter();
        CaminoSantiagoXacobeoApplicationService service = new CaminoSantiagoXacobeoApplicationService(repo);

        CaminoSantiagoXacobeo created = service.createCaminoSantiagoXacobeo("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<CaminoSantiagoXacobeo> found = service.findCaminoSantiagoXacobeoById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        CaminoSantiagoXacobeo optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
