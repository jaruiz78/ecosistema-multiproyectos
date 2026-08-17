package com.corp.proyectoemergencygeogrid.domain;

import com.corp.proyectoemergencygeogrid.domain.model.EmergencyDisasterCell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico EmergencyDisasterCell.
 * Zero-Mockito Policy.
 */
class EmergencyDisasterCellDomainTest {

    @Test
    @DisplayName("Debe instanciar EmergencyDisasterCell válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        EmergencyDisasterCell agg = new EmergencyDisasterCell("88390cb643fffff", "WILDFIRE", 4, 2500, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en EmergencyDisasterCell")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new EmergencyDisasterCell("88390cb643fffff", "WILDFIRE", 9, 2500, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
