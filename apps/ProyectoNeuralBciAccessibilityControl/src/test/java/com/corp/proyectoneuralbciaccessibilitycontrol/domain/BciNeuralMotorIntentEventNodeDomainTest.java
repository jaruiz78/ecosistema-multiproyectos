package com.corp.proyectoneuralbciaccessibilitycontrol.domain;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BciNeuralMotorIntentEventNodeDomainTest {

    @Test
    @DisplayName("Debe instanciar la entidad correctamente cumpliendo invariantes de dominio")
    void shouldCreateValidEntity() {
        BciNeuralMotorIntentEventNode entity = new BciNeuralMotorIntentEventNode(
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
        assertThatThrownBy(() -> new BciNeuralMotorIntentEventNode(
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
