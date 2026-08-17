package com.corp.proyectocirculartextiledpp.application;

import com.corp.proyectocirculartextiledpp.application.service.CircularTextileDPPApplicationService;
import com.corp.proyectocirculartextiledpp.domain.model.CircularTextileDPP;
import com.corp.proyectocirculartextiledpp.infrastructure.adapter.out.persistence.InMemoryCircularTextileDPPRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CircularTextileDPPApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de CircularTextileDPP usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCircularTextileDPPRepositoryAdapter repo = new InMemoryCircularTextileDPPRepositoryAdapter();
        CircularTextileDPPApplicationService service = new CircularTextileDPPApplicationService(repo);

        CircularTextileDPP created = service.createCircularTextileDPP("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<CircularTextileDPP> found = service.findCircularTextileDPPById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        CircularTextileDPP optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
