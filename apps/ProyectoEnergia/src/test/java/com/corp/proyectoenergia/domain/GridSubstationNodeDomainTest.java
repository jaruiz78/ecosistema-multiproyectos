package com.corp.proyectoenergia.domain;

import com.corp.proyectoenergia.domain.model.GridSubstationNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico GridSubstationNode.
 * Zero-Mockito Policy.
 */
class GridSubstationNodeDomainTest {

    @Test
    @DisplayName("Debe instanciar GridSubstationNode válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        GridSubstationNode agg = new GridSubstationNode("SUB-01", "Zone-North", 15000.0, 8500.0, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en GridSubstationNode")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new GridSubstationNode("SUB-01", "Zone-North", -500.0, 8500.0, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
