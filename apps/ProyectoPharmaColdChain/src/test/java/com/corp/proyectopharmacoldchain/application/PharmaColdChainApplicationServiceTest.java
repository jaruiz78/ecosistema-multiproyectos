package com.corp.proyectopharmacoldchain.application;

import com.corp.proyectopharmacoldchain.application.service.PharmaColdChainApplicationService;
import com.corp.proyectopharmacoldchain.domain.model.PharmaColdChain;
import com.corp.proyectopharmacoldchain.infrastructure.adapter.out.persistence.InMemoryPharmaColdChainRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PharmaColdChainApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de PharmaColdChain usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryPharmaColdChainRepositoryAdapter repo = new InMemoryPharmaColdChainRepositoryAdapter();
        PharmaColdChainApplicationService service = new PharmaColdChainApplicationService(repo);

        PharmaColdChain created = service.createPharmaColdChain("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<PharmaColdChain> found = service.findPharmaColdChainById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        PharmaColdChain optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
