package com.corp.proyectocriticalmineralsmrv.application;

import com.corp.proyectocriticalmineralsmrv.application.service.CriticalMineralsMRVApplicationService;
import com.corp.proyectocriticalmineralsmrv.domain.model.CriticalMineralsMRV;
import com.corp.proyectocriticalmineralsmrv.infrastructure.adapter.out.persistence.InMemoryCriticalMineralsMRVRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CriticalMineralsMRVApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de CriticalMineralsMRV usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryCriticalMineralsMRVRepositoryAdapter repo = new InMemoryCriticalMineralsMRVRepositoryAdapter();
        CriticalMineralsMRVApplicationService service = new CriticalMineralsMRVApplicationService(repo);

        CriticalMineralsMRV created = service.createCriticalMineralsMRV("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<CriticalMineralsMRV> found = service.findCriticalMineralsMRVById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        CriticalMineralsMRV optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
