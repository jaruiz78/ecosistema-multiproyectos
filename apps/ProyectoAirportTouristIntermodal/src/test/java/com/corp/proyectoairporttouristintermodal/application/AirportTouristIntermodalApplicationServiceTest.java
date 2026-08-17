package com.corp.proyectoairporttouristintermodal.application;

import com.corp.proyectoairporttouristintermodal.application.service.AirportTouristIntermodalApplicationService;
import com.corp.proyectoairporttouristintermodal.domain.model.AirportTouristIntermodal;
import com.corp.proyectoairporttouristintermodal.infrastructure.adapter.out.persistence.InMemoryAirportTouristIntermodalRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AirportTouristIntermodalApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de AirportTouristIntermodal usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryAirportTouristIntermodalRepositoryAdapter repo = new InMemoryAirportTouristIntermodalRepositoryAdapter();
        AirportTouristIntermodalApplicationService service = new AirportTouristIntermodalApplicationService(repo);

        AirportTouristIntermodal created = service.createAirportTouristIntermodal("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<AirportTouristIntermodal> found = service.findAirportTouristIntermodalById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        AirportTouristIntermodal optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
