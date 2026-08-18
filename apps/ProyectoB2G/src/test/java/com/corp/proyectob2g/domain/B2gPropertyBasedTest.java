package com.corp.proyectob2g.domain;

import com.corp.proyectob2g.domain.model.B2G;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class B2gPropertyBasedTest {

    @Test
    @DisplayName("Property Test: Para toda licitación B2G con presupuesto >= 0 la entidad es válida (1.000 iteraciones)")
    void propertyTestValidProcurementBudget() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double budgetEuros = rng.nextDouble() * 10_000_000.0;
            String id = "licitacion-" + i;
            String tenant = "ayto-tenant-" + (i % 12);

            B2G entity = new B2G(id, tenant, "Expediente " + i, budgetEuros, "ACTIVE", Instant.now());
            assertThat(entity.id()).isEqualTo(id);
            assertThat(entity.tenantId()).isEqualTo(tenant);
            assertThat(entity.value()).isEqualTo(budgetEuros);
        }
    }

    @Test
    @DisplayName("Property Test: Para presupuestos negativos se rechaza con excepción (1.000 iteraciones)")
    void propertyTestNegativeBudgetRejection() {
        Random rng = new Random(2026);
        for (int i = 0; i < 1_000; i++) {
            double negBudget = -(rng.nextDouble() * 50_000.0 + 0.001);
            String id = "err-b2g-" + i;

            assertThatThrownBy(() -> new B2G(id, "ayto-test", "Invalid Licitacion", negBudget, "ACTIVE", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
