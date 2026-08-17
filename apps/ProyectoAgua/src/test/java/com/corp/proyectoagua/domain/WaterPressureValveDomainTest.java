package com.corp.proyectoagua.domain;

import com.corp.proyectoagua.domain.model.WaterPressureValve;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico WaterPressureValve.
 * Zero-Mockito Policy.
 */
class WaterPressureValveDomainTest {

    @Test
    @DisplayName("Debe instanciar WaterPressureValve válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        WaterPressureValve agg = new WaterPressureValve("VLV-01", "SEG-CANAL-4", 4.2, 120.0, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en WaterPressureValve")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new WaterPressureValve("VLV-01", "SEG-CANAL-4", -1.0, 120.0, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
