package com.corp.proyectosyntheticbiologyfoundry.application;

import com.corp.proyectosyntheticbiologyfoundry.application.service.SyntheticBiologyFoundryApplicationService;
import com.corp.proyectosyntheticbiologyfoundry.domain.model.SyntheticBiologyFoundry;
import com.corp.proyectosyntheticbiologyfoundry.infrastructure.adapter.out.persistence.InMemorySyntheticBiologyFoundryRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SyntheticBiologyFoundryApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SyntheticBiologyFoundry usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySyntheticBiologyFoundryRepositoryAdapter repo = new InMemorySyntheticBiologyFoundryRepositoryAdapter();
        SyntheticBiologyFoundryApplicationService service = new SyntheticBiologyFoundryApplicationService(repo);

        SyntheticBiologyFoundry created = service.createSyntheticBiologyFoundry("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SyntheticBiologyFoundry> found = service.findSyntheticBiologyFoundryById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SyntheticBiologyFoundry optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
