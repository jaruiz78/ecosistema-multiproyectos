package com.corp.proyectoredparadorestwin.application;

import com.corp.proyectoredparadorestwin.application.service.RedParadoresTwinApplicationService;
import com.corp.proyectoredparadorestwin.domain.model.RedParadoresTwin;
import com.corp.proyectoredparadorestwin.infrastructure.adapter.out.persistence.InMemoryRedParadoresTwinRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RedParadoresTwinApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de RedParadoresTwin usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryRedParadoresTwinRepositoryAdapter repo = new InMemoryRedParadoresTwinRepositoryAdapter();
        RedParadoresTwinApplicationService service = new RedParadoresTwinApplicationService(repo);

        RedParadoresTwin created = service.createRedParadoresTwin("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<RedParadoresTwin> found = service.findRedParadoresTwinById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        RedParadoresTwin optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
