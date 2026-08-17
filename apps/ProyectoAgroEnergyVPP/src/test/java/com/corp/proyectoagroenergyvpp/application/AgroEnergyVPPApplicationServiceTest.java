package com.corp.proyectoagroenergyvpp.application;

import com.corp.proyectoagroenergyvpp.application.service.AgroEnergyVPPApplicationService;
import com.corp.proyectoagroenergyvpp.domain.model.AgroEnergyVPP;
import com.corp.proyectoagroenergyvpp.infrastructure.adapter.out.persistence.InMemoryAgroEnergyVPPRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AgroEnergyVPPApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de AgroEnergyVPP usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryAgroEnergyVPPRepositoryAdapter repo = new InMemoryAgroEnergyVPPRepositoryAdapter();
        AgroEnergyVPPApplicationService service = new AgroEnergyVPPApplicationService(repo);

        AgroEnergyVPP created = service.createAgroEnergyVPP("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<AgroEnergyVPP> found = service.findAgroEnergyVPPById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        AgroEnergyVPP optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
