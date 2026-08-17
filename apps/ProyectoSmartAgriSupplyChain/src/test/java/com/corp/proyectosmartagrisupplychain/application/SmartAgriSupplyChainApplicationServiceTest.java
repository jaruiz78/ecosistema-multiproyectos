package com.corp.proyectosmartagrisupplychain.application;

import com.corp.proyectosmartagrisupplychain.application.service.SmartAgriSupplyChainApplicationService;
import com.corp.proyectosmartagrisupplychain.domain.model.SmartAgriSupplyChain;
import com.corp.proyectosmartagrisupplychain.infrastructure.adapter.out.persistence.InMemorySmartAgriSupplyChainRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SmartAgriSupplyChainApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SmartAgriSupplyChain usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySmartAgriSupplyChainRepositoryAdapter repo = new InMemorySmartAgriSupplyChainRepositoryAdapter();
        SmartAgriSupplyChainApplicationService service = new SmartAgriSupplyChainApplicationService(repo);

        SmartAgriSupplyChain created = service.createSmartAgriSupplyChain("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SmartAgriSupplyChain> found = service.findSmartAgriSupplyChainById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SmartAgriSupplyChain optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
