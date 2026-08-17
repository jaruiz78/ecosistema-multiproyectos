package com.corp.proyectotokenrwa.domain;

import com.corp.proyectotokenrwa.domain.model.EscrowAssetVault;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico EscrowAssetVault.
 * Zero-Mockito Policy.
 */
class EscrowAssetVaultDomainTest {

    @Test
    @DisplayName("Debe instanciar EscrowAssetVault válidamente cumpliendo invariantes")
    void shouldCreateValid() {
        EscrowAssetVault agg = new EscrowAssetVault("VLT-01", "RWA-GOLD-01", 1250000.0, true, Instant.now());
        assertThat(agg).isNotNull();
    }

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en EscrowAssetVault")
    void shouldRejectInvalid() {
        assertThatThrownBy(() -> {
            new EscrowAssetVault("VLT-01", "RWA-GOLD-01", -50.0, true, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
