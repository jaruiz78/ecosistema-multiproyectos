package com.corp.proyectoterahertzmolecularscanner.domain;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TerahertzAbsorptionSpectrumNodePropertyBasedTest {

    @Test
    @DisplayName("Property Test: Para todo valor >= 0 la entidad es inmutable y válida (1.000 iteraciones)")
    void propertyTestValidValues() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double val = rng.nextDouble() * 100_000.0;
            String id = "id-" + i;
            String tenant = "tenant-" + (i % 10);
            
            TerahertzAbsorptionSpectrumNode entity = new TerahertzAbsorptionSpectrumNode(id, tenant, "Desc " + i, val, "ACTIVE", Instant.now());
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
            
            assertThatThrownBy(() -> new TerahertzAbsorptionSpectrumNode(id, "tenant-test", "Invalid", negVal, "ACTIVE", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
