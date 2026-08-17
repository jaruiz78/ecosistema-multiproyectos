package com.corp.proyectoairlineinterlinebaggage.application;

import com.corp.proyectoairlineinterlinebaggage.application.service.AirlineInterlineBaggageApplicationService;
import com.corp.proyectoairlineinterlinebaggage.domain.model.AirlineInterlineBaggage;
import com.corp.proyectoairlineinterlinebaggage.infrastructure.adapter.out.persistence.InMemoryAirlineInterlineBaggageRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AirlineInterlineBaggageApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de AirlineInterlineBaggage usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryAirlineInterlineBaggageRepositoryAdapter repo = new InMemoryAirlineInterlineBaggageRepositoryAdapter();
        AirlineInterlineBaggageApplicationService service = new AirlineInterlineBaggageApplicationService(repo);

        AirlineInterlineBaggage created = service.createAirlineInterlineBaggage("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<AirlineInterlineBaggage> found = service.findAirlineInterlineBaggageById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        AirlineInterlineBaggage optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
