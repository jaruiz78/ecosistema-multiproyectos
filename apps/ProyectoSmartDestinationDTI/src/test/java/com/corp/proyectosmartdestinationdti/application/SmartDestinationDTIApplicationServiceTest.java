package com.corp.proyectosmartdestinationdti.application;

import com.corp.proyectosmartdestinationdti.application.service.SmartDestinationDTIApplicationService;
import com.corp.proyectosmartdestinationdti.domain.model.SmartDestinationDTI;
import com.corp.proyectosmartdestinationdti.infrastructure.adapter.out.persistence.InMemorySmartDestinationDTIRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SmartDestinationDTIApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de SmartDestinationDTI usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemorySmartDestinationDTIRepositoryAdapter repo = new InMemorySmartDestinationDTIRepositoryAdapter();
        SmartDestinationDTIApplicationService service = new SmartDestinationDTIApplicationService(repo);

        SmartDestinationDTI created = service.createSmartDestinationDTI("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<SmartDestinationDTI> found = service.findSmartDestinationDTIById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        SmartDestinationDTI optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
