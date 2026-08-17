package com.corp.proyectoparquesnacionalesnatura2000.domain;

import com.corp.proyectoparquesnacionalesnatura2000.domain.model.ParquesNacionalesNatura2000;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParquesNacionalesNatura2000DomainTest {

    @Test
    @DisplayName("Debe crear la entidad ParquesNacionalesNatura2000 con invariantes válidos")
    void shouldCreateValidEntity() {
        ParquesNacionalesNatura2000 entity = new ParquesNacionalesNatura2000(
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
        assertThatThrownBy(() -> new ParquesNacionalesNatura2000(
            "id-002",
            "tenant-alpha",
            "Asset Title",
            -5.0,
            "ACTIVE",
            Instant.now()
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
