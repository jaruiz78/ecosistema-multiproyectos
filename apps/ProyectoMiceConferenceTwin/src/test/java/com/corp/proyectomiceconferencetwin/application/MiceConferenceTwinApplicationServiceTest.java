package com.corp.proyectomiceconferencetwin.application;

import com.corp.proyectomiceconferencetwin.application.service.MiceConferenceTwinApplicationService;
import com.corp.proyectomiceconferencetwin.domain.model.MiceConferenceTwin;
import com.corp.proyectomiceconferencetwin.infrastructure.adapter.out.persistence.InMemoryMiceConferenceTwinRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MiceConferenceTwinApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de MiceConferenceTwin usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryMiceConferenceTwinRepositoryAdapter repo = new InMemoryMiceConferenceTwinRepositoryAdapter();
        MiceConferenceTwinApplicationService service = new MiceConferenceTwinApplicationService(repo);

        MiceConferenceTwin created = service.createMiceConferenceTwin("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<MiceConferenceTwin> found = service.findMiceConferenceTwinById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        MiceConferenceTwin optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
