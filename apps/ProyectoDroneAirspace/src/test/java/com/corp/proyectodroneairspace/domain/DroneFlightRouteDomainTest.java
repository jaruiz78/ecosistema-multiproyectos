package com.corp.proyectodroneairspace.domain;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio Puro (Zero-Mockito Policy).
 * Verifica invariantes y comportamiento de DroneFlightRoute sin dependencias externas.
 */
class DroneFlightRouteDomainTest {

    @Test
    @DisplayName("Debe instanciar correctamente la entidad de dominio con datos válidos")
    void shouldCreateValidEntity() {
        DroneFlightRoute entity = new DroneFlightRoute(
            "item-001",
            "tenant-alpha",
            "Test Asset",
            150.0,
            "ACTIVE",
            Instant.now()
        );

        assertThat(entity.id()).isEqualTo("item-001");
        assertThat(entity.tenantId()).isEqualTo("tenant-alpha");
        assertThat(entity.value()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("Debe rechazar valores negativos por invariante de negocio")
    void shouldRejectNegativeValue() {
        assertThatThrownBy(() -> new DroneFlightRoute(
            "item-002",
            "tenant-alpha",
            "Invalid Asset",
            -10.0,
            "ACTIVE",
            Instant.now()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("no puede ser negativo");
    }
}
