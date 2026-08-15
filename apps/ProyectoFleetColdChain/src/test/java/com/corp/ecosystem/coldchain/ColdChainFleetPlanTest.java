package com.corp.ecosystem.coldchain;

import com.corp.ecosystem.coldchain.application.ColdChainFleetService;
import com.corp.ecosystem.coldchain.domain.ColdChainFleetPlan;
import com.corp.ecosystem.coldchain.domain.port.FleetDispatchRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoFleetColdChain.
 */
class ColdChainFleetPlanTest {

    static class InMemoryFleetRepository implements FleetDispatchRepositoryPort {
        private final Map<ColdChainFleetPlan.RoutePlanId, ColdChainFleetPlan> storage = new ConcurrentHashMap<>();

        @Override
        public ColdChainFleetPlan save(ColdChainFleetPlan plan) {
            storage.put(plan.id(), plan);
            return plan;
        }

        @Override
        public Optional<ColdChainFleetPlan> findById(ColdChainFleetPlan.RoutePlanId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryFleetRepository repository = new InMemoryFleetRepository();
    private final ColdChainFleetService service = new ColdChainFleetService(repository);

    @Test
    @DisplayName("Debe crear un plan de transporte de medicamentos refrigerados (2C a 8C)")
    void shouldCreatePharmaRefrigeratedPlan() {
        ColdChainFleetPlan plan = service.createPlan(
                "pharma-dist-01",
                "VAN-COOL-404",
                ColdChainFleetPlan.TemperatureCategory.REFRIGERATED_PHARMA,
                List.of(new ColdChainFleetPlan.DeliveryStop("STOP-1", 0x88390cb307fffffL, 3600, 7200, false))
        );

        assertNotNull(plan.id());
        assertEquals(2.0, plan.thermalRange().minAllowedCelsius());
        assertEquals(8.0, plan.thermalRange().maxAllowedCelsius());
        assertEquals(ColdChainFleetPlan.PlanState.IN_TRANSIT, plan.state());
    }

    @Test
    @DisplayName("Debe detectar excursión térmica si la temperatura sube a 11.5C")
    void shouldDetectThermalExcursionWhenTempExceedsRange() {
        ColdChainFleetPlan plan = service.createPlan(
                "pharma-dist-01",
                "VAN-COOL-404",
                ColdChainFleetPlan.TemperatureCategory.REFRIGERATED_PHARMA,
                List.of()
        );

        // Lectura normal: 4.5C
        service.ingestThermalTelemetry(plan.id(), 4.5, 55.0);
        // Excursión: 11.5C
        ColdChainFleetPlan updated = service.ingestThermalTelemetry(plan.id(), 11.5, 60.0);

        assertEquals(ColdChainFleetPlan.PlanState.COMPLETED_WITH_EXCURSION, updated.state());
        assertEquals(2, updated.telemetryLog().size());
        assertTrue(updated.telemetryLog().get(1).isExcursion());
    }
}
