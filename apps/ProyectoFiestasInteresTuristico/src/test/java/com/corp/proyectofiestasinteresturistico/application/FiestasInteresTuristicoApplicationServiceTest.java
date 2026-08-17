package com.corp.proyectofiestasinteresturistico.application;

import com.corp.proyectofiestasinteresturistico.application.service.FiestasInteresTuristicoApplicationService;
import com.corp.proyectofiestasinteresturistico.domain.model.FiestasInteresTuristico;
import com.corp.proyectofiestasinteresturistico.infrastructure.adapter.out.persistence.InMemoryFiestasInteresTuristicoRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FiestasInteresTuristicoApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de FiestasInteresTuristico usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryFiestasInteresTuristicoRepositoryAdapter repo = new InMemoryFiestasInteresTuristicoRepositoryAdapter();
        FiestasInteresTuristicoApplicationService service = new FiestasInteresTuristicoApplicationService(repo);

        FiestasInteresTuristico created = service.createFiestasInteresTuristico("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<FiestasInteresTuristico> found = service.findFiestasInteresTuristicoById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        FiestasInteresTuristico optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
