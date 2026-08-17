package com.corp.proyectocarbonledger.application;

import com.corp.proyectocarbonledger.application.service.CarbonLedgerApplicationService;
import com.corp.proyectocarbonledger.domain.model.CarbonLedger;
import com.corp.proyectocarbonledger.infrastructure.adapter.out.persistence.InMemoryCarbonLedgerRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CarbonLedgerApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de CarbonLedger usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCarbonLedgerRepositoryAdapter repo = new InMemoryCarbonLedgerRepositoryAdapter();
        CarbonLedgerApplicationService service = new CarbonLedgerApplicationService(repo);

        CarbonLedger created = service.createCarbonLedger("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<CarbonLedger> found = service.findCarbonLedgerById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        CarbonLedger optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
