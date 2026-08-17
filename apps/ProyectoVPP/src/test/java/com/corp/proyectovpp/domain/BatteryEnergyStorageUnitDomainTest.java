package com.corp.proyectovpp.domain;

import com.corp.proyectovpp.domain.model.BatteryEnergyStorageUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico BatteryEnergyStorageUnit.
 * Zero-Mockito Policy.
 */
class BatteryEnergyStorageUnitDomainTest {

    @Test
    @DisplayName("Debe instanciar BatteryEnergyStorageUnit válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        BatteryEnergyStorageUnit agg = new BatteryEnergyStorageUnit("BAT-01", "LFP", 85.5, 250.0, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en BatteryEnergyStorageUnit")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new BatteryEnergyStorageUnit("BAT-01", "LFP", 150.0, 250.0, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
