package com.corp.proyectoenergia.domain;

import com.corp.proyectoenergia.domain.model.Energia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnergiaPropertyBasedTest {

    @Test
    @DisplayName("Property Test: Para todo valor de energía >= 0 kWh la entidad es válida (1.000 iteraciones)")
    void propertyTestValidEnergyValues() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double val = rng.nextDouble() * 500_000.0;
            String id = "pwr-" + i;
            String tenant = "grid-tenant-" + (i % 8);

            Energia entity = new Energia(id, tenant, "Solar Array " + i, val, "ACTIVE", Instant.now());
            assertThat(entity.id()).isEqualTo(id);
            assertThat(entity.tenantId()).isEqualTo(tenant);
            assertThat(entity.value()).isEqualTo(val);
        }
    }

    @Test
    @DisplayName("Property Test: Para todo valor negativo se garantiza rechazo con IllegalArgumentException (1.000 iteraciones)")
    void propertyTestNegativeEnergyRejection() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double negVal = -(rng.nextDouble() * 10_000.0 + 0.001);
            String id = "err-pwr-" + i;

            assertThatThrownBy(() -> new Energia(id, "grid-tenant-1", "Invalid BESS", negVal, "ACTIVE", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
