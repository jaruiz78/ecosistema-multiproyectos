package com.corp.proyectoglobalcruisemrv.application;

import com.corp.proyectoglobalcruisemrv.application.service.GlobalCruiseMRVApplicationService;
import com.corp.proyectoglobalcruisemrv.domain.model.GlobalCruiseMRV;
import com.corp.proyectoglobalcruisemrv.infrastructure.adapter.out.persistence.InMemoryGlobalCruiseMRVRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalCruiseMRVApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de GlobalCruiseMRV usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryGlobalCruiseMRVRepositoryAdapter repo = new InMemoryGlobalCruiseMRVRepositoryAdapter();
        GlobalCruiseMRVApplicationService service = new GlobalCruiseMRVApplicationService(repo);

        GlobalCruiseMRV created = service.createGlobalCruiseMRV("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<GlobalCruiseMRV> found = service.findGlobalCruiseMRVById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        GlobalCruiseMRV optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
