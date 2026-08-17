package com.corp.proyectoindustrialmicrogridmpc.domain;

import com.corp.proyectoindustrialmicrogridmpc.domain.model.IndustrialMicrogridMPC;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IndustrialMicrogridMPCDomainTest {

    @Test
    @DisplayName("Debe crear la entidad IndustrialMicrogridMPC con invariantes válidos")
    void shouldCreateValidEntity() {
        IndustrialMicrogridMPC entity = new IndustrialMicrogridMPC(
            "id-001",
            "tenant-alpha",
            "Asset Title",
            100.0,
            "ACTIVE",
            Instant.now()
        );

        assertThat(entity.id()).isEqualTo("id-001");
        assertThat(entity.tenantId()).isEqualTo("tenant-alpha");
        assertThat(entity.value()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Debe rechazar valor negativo por invariante de dominio")
    void shouldRejectNegativeValue() {
        assertThatThrownBy(() -> new IndustrialMicrogridMPC(
            "id-002",
            "tenant-alpha",
            "Asset Title",
            -5.0,
            "ACTIVE",
            Instant.now()
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
