package com.corp.proyectovpp.domain;

import com.corp.proyectovpp.domain.model.VPP;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VppPropertyBasedTest {

    @Test
    @DisplayName("Property Test: Para todo agregado DER >= 0 kW la entidad VPP es válida (1.000 iteraciones)")
    void propertyTestValidVppAggregation() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double capacityKw = rng.nextDouble() * 1_000_000.0;
            String id = "vpp-cluster-" + i;
            String tenant = "vpp-tenant-" + (i % 5);

            VPP entity = new VPP(id, tenant, "VPP Node " + i, capacityKw, "ACTIVE", Instant.now());
            assertThat(entity.id()).isEqualTo(id);
            assertThat(entity.tenantId()).isEqualTo(tenant);
            assertThat(entity.value()).isEqualTo(capacityKw);
        }
    }

    @Test
    @DisplayName("Property Test: Para todo valor negativo de capacidad DER se rechaza con excepción (1.000 iteraciones)")
    void propertyTestNegativeVppRejection() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double negVal = -(rng.nextDouble() * 10_000.0 + 0.001);
            String id = "err-vpp-" + i;

            assertThatThrownBy(() -> new VPP(id, "tenant-err", "Invalid DER", negVal, "ACTIVE", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
