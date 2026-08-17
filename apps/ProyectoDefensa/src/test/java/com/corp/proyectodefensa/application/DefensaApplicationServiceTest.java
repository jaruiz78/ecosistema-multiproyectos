package com.corp.proyectodefensa.application;

import com.corp.proyectodefensa.application.service.DefensaApplicationService;
import com.corp.proyectodefensa.domain.model.Defensa;
import com.corp.proyectodefensa.infrastructure.adapter.out.persistence.InMemoryDefensaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DefensaApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de Defensa usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryDefensaRepositoryAdapter repo = new InMemoryDefensaRepositoryAdapter();
        DefensaApplicationService service = new DefensaApplicationService(repo);

        Defensa created = service.createDefensa("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<Defensa> found = service.findDefensaById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        Defensa optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
