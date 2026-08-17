package com.corp.proyectoenoturismorutasvino.application;

import com.corp.proyectoenoturismorutasvino.application.service.EnoturismoRutasVinoApplicationService;
import com.corp.proyectoenoturismorutasvino.domain.model.EnoturismoRutasVino;
import com.corp.proyectoenoturismorutasvino.infrastructure.adapter.out.persistence.InMemoryEnoturismoRutasVinoRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EnoturismoRutasVinoApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de EnoturismoRutasVino usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryEnoturismoRutasVinoRepositoryAdapter repo = new InMemoryEnoturismoRutasVinoRepositoryAdapter();
        EnoturismoRutasVinoApplicationService service = new EnoturismoRutasVinoApplicationService(repo);

        EnoturismoRutasVino created = service.createEnoturismoRutasVino("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<EnoturismoRutasVino> found = service.findEnoturismoRutasVinoById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        EnoturismoRutasVino optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
