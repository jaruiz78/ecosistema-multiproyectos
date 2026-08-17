package com.corp.proyectohoteltwinrevpar.application;

import com.corp.proyectohoteltwinrevpar.application.service.HotelTwinRevPARApplicationService;
import com.corp.proyectohoteltwinrevpar.domain.model.HotelTwinRevPAR;
import com.corp.proyectohoteltwinrevpar.infrastructure.adapter.out.persistence.InMemoryHotelTwinRevPARRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HotelTwinRevPARApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de HotelTwinRevPAR usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryHotelTwinRevPARRepositoryAdapter repo = new InMemoryHotelTwinRevPARRepositoryAdapter();
        HotelTwinRevPARApplicationService service = new HotelTwinRevPARApplicationService(repo);

        HotelTwinRevPAR created = service.createHotelTwinRevPAR("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<HotelTwinRevPAR> found = service.findHotelTwinRevPARById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        HotelTwinRevPAR optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
