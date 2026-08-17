package com.corp.proyectoplayasinteligentescostas.application;

import com.corp.proyectoplayasinteligentescostas.application.service.PlayasInteligentesCostasApplicationService;
import com.corp.proyectoplayasinteligentescostas.domain.model.PlayasInteligentesCostas;
import com.corp.proyectoplayasinteligentescostas.infrastructure.adapter.out.persistence.InMemoryPlayasInteligentesCostasRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayasInteligentesCostasApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de PlayasInteligentesCostas usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryPlayasInteligentesCostasRepositoryAdapter repo = new InMemoryPlayasInteligentesCostasRepositoryAdapter();
        PlayasInteligentesCostasApplicationService service = new PlayasInteligentesCostasApplicationService(repo);

        PlayasInteligentesCostas created = service.createPlayasInteligentesCostas("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<PlayasInteligentesCostas> found = service.findPlayasInteligentesCostasById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        PlayasInteligentesCostas optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
