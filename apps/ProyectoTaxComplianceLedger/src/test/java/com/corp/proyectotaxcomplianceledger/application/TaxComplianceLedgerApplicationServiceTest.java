package com.corp.proyectotaxcomplianceledger.application;

import com.corp.proyectotaxcomplianceledger.application.service.TaxComplianceLedgerApplicationService;
import com.corp.proyectotaxcomplianceledger.domain.model.TaxComplianceLedger;
import com.corp.proyectotaxcomplianceledger.infrastructure.adapter.out.persistence.InMemoryTaxComplianceLedgerRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaxComplianceLedgerApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de TaxComplianceLedger usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryTaxComplianceLedgerRepositoryAdapter repo = new InMemoryTaxComplianceLedgerRepositoryAdapter();
        TaxComplianceLedgerApplicationService service = new TaxComplianceLedgerApplicationService(repo);

        TaxComplianceLedger created = service.createTaxComplianceLedger("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<TaxComplianceLedger> found = service.findTaxComplianceLedgerById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        TaxComplianceLedger optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
