package com.corp.proyectofleetcoldchain.application;

import com.corp.proyectofleetcoldchain.application.service.FleetColdChainApplicationService;
import com.corp.proyectofleetcoldchain.domain.model.FleetColdChain;
import com.corp.proyectofleetcoldchain.infrastructure.adapter.out.persistence.InMemoryFleetColdChainRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FleetColdChainApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de FleetColdChain usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryFleetColdChainRepositoryAdapter repo = new InMemoryFleetColdChainRepositoryAdapter();
        FleetColdChainApplicationService service = new FleetColdChainApplicationService(repo);

        FleetColdChain created = service.createFleetColdChain("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<FleetColdChain> found = service.findFleetColdChainById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        FleetColdChain optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
