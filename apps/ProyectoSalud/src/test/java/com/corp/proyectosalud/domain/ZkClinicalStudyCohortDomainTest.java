package com.corp.proyectosalud.domain;

import com.corp.proyectosalud.domain.model.ZkClinicalStudyCohort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico ZkClinicalStudyCohort.
 * Zero-Mockito Policy.
 */
class ZkClinicalStudyCohortDomainTest {

    @Test
    @DisplayName("Debe instanciar ZkClinicalStudyCohort válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        ZkClinicalStudyCohort agg = new ZkClinicalStudyCohort("COH-01", "0xdeadbeef1234", 1500, true, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en ZkClinicalStudyCohort")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new ZkClinicalStudyCohort("COH-01", "0xdeadbeef1234", -10, true, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
