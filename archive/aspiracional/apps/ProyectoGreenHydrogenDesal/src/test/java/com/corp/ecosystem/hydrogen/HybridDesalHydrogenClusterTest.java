package com.corp.ecosystem.hydrogen;

import com.corp.ecosystem.hydrogen.application.GreenHydrogenDesalService;
import com.corp.ecosystem.hydrogen.domain.HybridDesalHydrogenCluster;
import com.corp.ecosystem.hydrogen.domain.port.HybridPlantRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoGreenHydrogenDesal.
 */
class HybridDesalHydrogenClusterTest {

    static class InMemoryHybridPlantRepository implements HybridPlantRepositoryPort {
        private final Map<HybridDesalHydrogenCluster.PlantId, HybridDesalHydrogenCluster> storage = new ConcurrentHashMap<>();

        @Override
        public HybridDesalHydrogenCluster save(HybridDesalHydrogenCluster plant) {
            storage.put(plant.id(), plant);
            return plant;
        }

        @Override
        public Optional<HybridDesalHydrogenCluster> findById(HybridDesalHydrogenCluster.PlantId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryHybridPlantRepository repository = new InMemoryHybridPlantRepository();
    private final GreenHydrogenDesalService service = new GreenHydrogenDesalService(repository);

    @Test
    @DisplayName("Debe registrar y optimizar el despacho MPC priorizando hidrógeno ante precios bajos")
    void shouldOptimizeMpcDispatchWithLowElectricityPrice() {
        HybridDesalHydrogenCluster plant = service.registerHybridPlant(
                "iberdrola-andalucia",
                50.0,  // 50 MW electrolizador
                20000.0, // 20.000 m3/día desal
                100.0, // 100 MW solar
                50.0   // 50 MW eólica
        );

        assertNotNull(plant.id());

        // Despacho con 80 MW renovables y precio spot bajo (15 EUR/MWh)
        HybridDesalHydrogenCluster dispatched = service.dispatchMpc(plant.id(), 80.0, 15.0);

        assertTrue(dispatched.currentSetpoint().allocatedElectrolyzerMw() > 0);
        assertTrue(dispatched.currentSetpoint().hydrogenProductionKgHour() > 0);
        assertTrue(dispatched.currentSetpoint().estimatedHourlyProfitEur() > 0);
    }

    @Test
    @DisplayName("Debe modular la producción de H2 y mantener desalación base ante precios spot elevados")
    void shouldPrioritizeDesalinationOnHighElectricityPrice() {
        HybridDesalHydrogenCluster plant = service.registerHybridPlant(
                "acciona-canarias",
                20.0,
                15000.0,
                40.0,
                20.0
        );

        HybridDesalHydrogenCluster dispatched = service.dispatchMpc(plant.id(), 20.0, 95.0);

        assertTrue(dispatched.currentSetpoint().allocatedDesalMw() > 0);
        assertTrue(dispatched.currentSetpoint().desalWaterProductionM3Hour() > 0);
    }
}
