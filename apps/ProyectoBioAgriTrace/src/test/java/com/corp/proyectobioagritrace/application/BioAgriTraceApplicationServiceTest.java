package com.corp.proyectobioagritrace.application;

import com.corp.proyectobioagritrace.application.service.BioAgriTraceApplicationService;
import com.corp.proyectobioagritrace.domain.model.BioAgriTrace;
import com.corp.proyectobioagritrace.infrastructure.adapter.out.persistence.InMemoryBioAgriTraceRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BioAgriTraceApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de BioAgriTrace usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryBioAgriTraceRepositoryAdapter repo = new InMemoryBioAgriTraceRepositoryAdapter();
        BioAgriTraceApplicationService service = new BioAgriTraceApplicationService(repo);

        BioAgriTrace created = service.createBioAgriTrace("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<BioAgriTrace> found = service.findBioAgriTraceById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        BioAgriTrace optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
