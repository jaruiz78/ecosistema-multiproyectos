package com.corp.proyectourbanenergymobilitynexus.domain;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class V2GChargingDispatchNodeDomainTest {

    @Test
    @DisplayName("Debe instanciar la entidad correctamente cumpliendo invariantes de dominio")
    void shouldCreateValidEntity() {
        V2GChargingDispatchNode entity = new V2GChargingDispatchNode(
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
        assertThatThrownBy(() -> new V2GChargingDispatchNode(
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
