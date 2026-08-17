package com.corp.proyectoecotasasoberanatax.application;

import com.corp.proyectoecotasasoberanatax.application.service.EcotasaSoberanaTaxApplicationService;
import com.corp.proyectoecotasasoberanatax.domain.model.EcotasaSoberanaTax;
import com.corp.proyectoecotasasoberanatax.infrastructure.adapter.out.persistence.InMemoryEcotasaSoberanaTaxRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EcotasaSoberanaTaxApplicationServiceTest {

    @Test
    @DisplayName("Debe orquestar la creación y recuperación de EcotasaSoberanaTax usando adaptador in-memory")
    void shouldCreateAndRetrieveEntity() {
        InMemoryEcotasaSoberanaTaxRepositoryAdapter repo = new InMemoryEcotasaSoberanaTaxRepositoryAdapter();
        EcotasaSoberanaTaxApplicationService service = new EcotasaSoberanaTaxApplicationService(repo);

        EcotasaSoberanaTax created = service.createEcotasaSoberanaTax("tenant-test", "Orchestrated Asset", 250.0);
        assertThat(created).isNotNull();
        assertThat(created.tenantId()).isEqualTo("tenant-test");
        assertThat(created.status()).isEqualTo("CREATED");

        Optional<EcotasaSoberanaTax> found = service.findEcotasaSoberanaTaxById(created.id(), "tenant-test");
        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Orchestrated Asset");

        EcotasaSoberanaTax optimized = service.processOptimization(created.id(), "tenant-test");
        assertThat(optimized.status()).isEqualTo("OPTIMIZED");
    }
}
