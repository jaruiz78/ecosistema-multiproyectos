package com.corp.proyectodiputacionturismorural.application;

import com.corp.proyectodiputacionturismorural.application.service.DiputacionTurismoRuralApplicationService;
import com.corp.proyectodiputacionturismorural.domain.model.DiputacionTurismoRural;
import com.corp.proyectodiputacionturismorural.infrastructure.adapter.out.persistence.InMemoryDiputacionTurismoRuralRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DiputacionTurismoRuralApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de DiputacionTurismoRural usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryDiputacionTurismoRuralRepositoryAdapter repo = new InMemoryDiputacionTurismoRuralRepositoryAdapter();
        DiputacionTurismoRuralApplicationService service = new DiputacionTurismoRuralApplicationService(repo);

        DiputacionTurismoRural created = service.createDiputacionTurismoRural("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<DiputacionTurismoRural> found = service.findDiputacionTurismoRuralById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        DiputacionTurismoRural optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
