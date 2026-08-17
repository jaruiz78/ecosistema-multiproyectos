package com.corp.proyectocatastrofes.application;

import com.corp.proyectocatastrofes.application.service.CatastrofesApplicationService;
import com.corp.proyectocatastrofes.domain.model.Catastrofes;
import com.corp.proyectocatastrofes.infrastructure.adapter.out.persistence.InMemoryCatastrofesRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CatastrofesApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Catastrofes usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCatastrofesRepositoryAdapter repo = new InMemoryCatastrofesRepositoryAdapter();
        CatastrofesApplicationService service = new CatastrofesApplicationService(repo);

        Catastrofes created = service.createCatastrofes("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Catastrofes> found = service.findCatastrofesById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Catastrofes optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
