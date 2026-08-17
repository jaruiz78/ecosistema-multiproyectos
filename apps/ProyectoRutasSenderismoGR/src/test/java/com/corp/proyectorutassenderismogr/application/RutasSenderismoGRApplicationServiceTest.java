package com.corp.proyectorutassenderismogr.application;

import com.corp.proyectorutassenderismogr.application.service.RutasSenderismoGRApplicationService;
import com.corp.proyectorutassenderismogr.domain.model.RutasSenderismoGR;
import com.corp.proyectorutassenderismogr.infrastructure.adapter.out.persistence.InMemoryRutasSenderismoGRRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RutasSenderismoGRApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de RutasSenderismoGR usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryRutasSenderismoGRRepositoryAdapter repo = new InMemoryRutasSenderismoGRRepositoryAdapter();
        RutasSenderismoGRApplicationService service = new RutasSenderismoGRApplicationService(repo);

        RutasSenderismoGR created = service.createRutasSenderismoGR("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<RutasSenderismoGR> found = service.findRutasSenderismoGRById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        RutasSenderismoGR optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
