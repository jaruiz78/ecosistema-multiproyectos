package com.corp.proyectodiputacionturismorural.domain;

import com.corp.proyectodiputacionturismorural.domain.model.DiputacionTurismoRural;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiputacionTurismoRuralDomainTest {

    @Test
    @DisplayName("Debe crear la entidad DiputacionTurismoRural con invariantes válidos")
    void shouldCreateValidEntity() {
        DiputacionTurismoRural entity = new DiputacionTurismoRural(
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
        assertThatThrownBy(() -> new DiputacionTurismoRural(
            "id-002",
            "tenant-alpha",
            "Asset Title",
            -5.0,
            "ACTIVE",
            Instant.now()
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
