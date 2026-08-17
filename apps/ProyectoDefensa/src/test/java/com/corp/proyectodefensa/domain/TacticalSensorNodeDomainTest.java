package com.corp.proyectodefensa.domain;

import com.corp.proyectodefensa.domain.model.TacticalSensorNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico TacticalSensorNode.
 * Zero-Mockito Policy.
 */
class TacticalSensorNodeDomainTest {

    @Test
    @DisplayName("Debe instanciar TacticalSensorNode válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        TacticalSensorNode agg = new TacticalSensorNode("SNS-01", "RADAR_L", -45.0, true, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en TacticalSensorNode")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new TacticalSensorNode("SNS-01", "RADAR_L", 15.0, true, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
