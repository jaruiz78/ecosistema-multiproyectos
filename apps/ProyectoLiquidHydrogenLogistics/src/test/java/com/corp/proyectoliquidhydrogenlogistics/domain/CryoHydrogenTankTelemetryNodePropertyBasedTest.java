package com.corp.proyectoliquidhydrogenlogistics.domain;

import com.corp.proyectoliquidhydrogenlogistics.domain.model.CryoHydrogenTankTelemetryNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CryoHydrogenTankTelemetryNodePropertyBasedTest {

    @Test
    @DisplayName("Property Test: Para todo valor >= 0 la entidad es inmutable y válida (1.000 iteraciones)")
    void propertyTestValidValues() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double val = rng.nextDouble() * 100_000.0;
            String id = "id-" + i;
            String tenant = "tenant-" + (i % 10);
            
            CryoHydrogenTankTelemetryNode entity = new CryoHydrogenTankTelemetryNode(id, tenant, "Desc " + i, val, "ACTIVE", Instant.now());
            assertThat(entity.id()).isEqualTo(id);
            assertThat(entity.tenantId()).isEqualTo(tenant);
            assertThat(entity.value()).isEqualTo(val);
        }
    }

    @Test
    @DisplayName("Property Test: Para todo valor < 0 se garantiza rechazo con IllegalArgumentException (1.000 iteraciones)")
    void propertyTestNegativeValues() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double negVal = -(rng.nextDouble() * 10_000.0 + 0.001);
            String id = "err-" + i;
            
            assertThatThrownBy(() -> new CryoHydrogenTankTelemetryNode(id, "tenant-test", "Invalid", negVal, "ACTIVE", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
