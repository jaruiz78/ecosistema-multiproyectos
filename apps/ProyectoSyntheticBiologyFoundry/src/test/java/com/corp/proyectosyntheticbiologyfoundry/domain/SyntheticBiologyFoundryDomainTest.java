package com.corp.proyectosyntheticbiologyfoundry.domain;

import com.corp.proyectosyntheticbiologyfoundry.domain.model.SyntheticBiologyFoundry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SyntheticBiologyFoundryDomainTest {

    @Test
    @DisplayName("Debe crear la entidad SyntheticBiologyFoundry con invariantes válidos")
    void shouldCreateValidEntity() {
        SyntheticBiologyFoundry entity = new SyntheticBiologyFoundry(
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
        assertThatThrownBy(() -> new SyntheticBiologyFoundry(
            "id-002",
            "tenant-alpha",
            "Asset Title",
            -5.0,
            "ACTIVE",
            Instant.now()
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
