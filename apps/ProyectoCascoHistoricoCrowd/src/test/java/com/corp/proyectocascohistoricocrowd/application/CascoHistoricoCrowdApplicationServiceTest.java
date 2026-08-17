package com.corp.proyectocascohistoricocrowd.application;

import com.corp.proyectocascohistoricocrowd.application.service.CascoHistoricoCrowdApplicationService;
import com.corp.proyectocascohistoricocrowd.domain.model.CascoHistoricoCrowd;
import com.corp.proyectocascohistoricocrowd.infrastructure.adapter.out.persistence.InMemoryCascoHistoricoCrowdRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CascoHistoricoCrowdApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de CascoHistoricoCrowd usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCascoHistoricoCrowdRepositoryAdapter repo = new InMemoryCascoHistoricoCrowdRepositoryAdapter();
        CascoHistoricoCrowdApplicationService service = new CascoHistoricoCrowdApplicationService(repo);

        CascoHistoricoCrowd created = service.createCascoHistoricoCrowd("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<CascoHistoricoCrowd> found = service.findCascoHistoricoCrowdById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        CascoHistoricoCrowd optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
