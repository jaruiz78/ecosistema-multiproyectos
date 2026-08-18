package com.corp.proyectobluecarbonoceans.domain;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MarinePosidoniaCarbonSinkDomainTest {

    @Test
    @DisplayName("Debe instanciar la entidad correctamente cumpliendo invariantes de dominio")
    void shouldCreateValidEntity() {
        MarinePosidoniaCarbonSink entity = new MarinePosidoniaCarbonSink(
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
        assertThatThrownBy(() -> new MarinePosidoniaCarbonSink(
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
