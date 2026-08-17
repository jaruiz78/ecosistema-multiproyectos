package com.corp.proyectoecotourismpassport.application;

import com.corp.proyectoecotourismpassport.application.service.EcoTourismPassportApplicationService;
import com.corp.proyectoecotourismpassport.domain.model.EcoTourismPassport;
import com.corp.proyectoecotourismpassport.infrastructure.adapter.out.persistence.InMemoryEcoTourismPassportRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EcoTourismPassportApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de EcoTourismPassport usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryEcoTourismPassportRepositoryAdapter repo = new InMemoryEcoTourismPassportRepositoryAdapter();
        EcoTourismPassportApplicationService service = new EcoTourismPassportApplicationService(repo);

        EcoTourismPassport created = service.createEcoTourismPassport("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<EcoTourismPassport> found = service.findEcoTourismPassportById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        EcoTourismPassport optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
