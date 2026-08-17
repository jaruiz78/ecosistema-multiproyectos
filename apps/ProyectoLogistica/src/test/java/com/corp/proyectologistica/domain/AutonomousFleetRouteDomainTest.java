package com.corp.proyectologistica.domain;

import com.corp.proyectologistica.domain.model.AutonomousFleetRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico AutonomousFleetRoute.
 * Zero-Mockito Policy.
 */
class AutonomousFleetRouteDomainTest {

    @Test
    @DisplayName("Debe instanciar AutonomousFleetRoute válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        AutonomousFleetRoute agg = new AutonomousFleetRoute("RT-01", "88390cb643fffff", "88390cb647fffff", 12.5, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en AutonomousFleetRoute")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new AutonomousFleetRoute("RT-01", "88390cb643fffff", "88390cb647fffff", -2.0, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
