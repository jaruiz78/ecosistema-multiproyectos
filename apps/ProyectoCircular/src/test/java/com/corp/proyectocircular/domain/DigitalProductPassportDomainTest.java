package com.corp.proyectocircular.domain;

import com.corp.proyectocircular.domain.model.DigitalProductPassport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico DigitalProductPassport.
 * Zero-Mockito Policy.
 */
class DigitalProductPassportDomainTest {

    @Test
    @DisplayName("Debe instanciar DigitalProductPassport válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        DigitalProductPassport agg = new DigitalProductPassport("DPP-01", "BATCH-2026-A", 65.0, "https://passport.corp/dpp-01", Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en DigitalProductPassport")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new DigitalProductPassport("DPP-01", "BATCH-2026-A", 150.0, "https://passport.corp/dpp-01", Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
