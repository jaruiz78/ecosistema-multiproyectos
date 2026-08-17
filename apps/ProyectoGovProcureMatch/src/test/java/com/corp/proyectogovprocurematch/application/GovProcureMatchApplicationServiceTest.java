package com.corp.proyectogovprocurematch.application;

import com.corp.proyectogovprocurematch.application.service.GovProcureMatchApplicationService;
import com.corp.proyectogovprocurematch.domain.model.GovProcureMatch;
import com.corp.proyectogovprocurematch.infrastructure.adapter.out.persistence.InMemoryGovProcureMatchRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GovProcureMatchApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de GovProcureMatch usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryGovProcureMatchRepositoryAdapter repo = new InMemoryGovProcureMatchRepositoryAdapter();
        GovProcureMatchApplicationService service = new GovProcureMatchApplicationService(repo);

        GovProcureMatch created = service.createGovProcureMatch("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<GovProcureMatch> found = service.findGovProcureMatchById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        GovProcureMatch optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
