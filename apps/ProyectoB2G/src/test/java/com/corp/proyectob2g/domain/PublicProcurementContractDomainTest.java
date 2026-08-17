package com.corp.proyectob2g.domain;

import com.corp.proyectob2g.domain.model.PublicProcurementContract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico PublicProcurementContract.
 * Zero-Mockito Policy.
 */
class PublicProcurementContractDomainTest {

    @Test
    @DisplayName("Debe instanciar PublicProcurementContract válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        PublicProcurementContract agg = new PublicProcurementContract("CNT-01", "Ministerio de Transición", 500000.0, "PUBLISHED", Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en PublicProcurementContract")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new PublicProcurementContract("CNT-01", "Ministerio de Transición", -100.0, "PUBLISHED", Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
