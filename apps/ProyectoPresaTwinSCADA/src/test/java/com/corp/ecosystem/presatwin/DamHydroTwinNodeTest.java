package com.corp.ecosystem.presatwin;

import com.corp.ecosystem.presatwin.application.DamHydroTwinService;
import com.corp.ecosystem.presatwin.domain.DamHydroTwinNode;
import com.corp.ecosystem.presatwin.domain.port.DamTelemetryRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoPresaTwinSCADA.
 */
class DamHydroTwinNodeTest {

    static class InMemoryDamRepository implements DamTelemetryRepositoryPort {
        private final Map<DamHydroTwinNode.DamId, DamHydroTwinNode> storage = new ConcurrentHashMap<>();

        @Override
        public DamHydroTwinNode save(DamHydroTwinNode dam) {
            storage.put(dam.id(), dam);
            return dam;
        }

        @Override
        public Optional<DamHydroTwinNode> findById(DamHydroTwinNode.DamId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryDamRepository repository = new InMemoryDamRepository();
    private final DamHydroTwinService service = new DamHydroTwinService(repository);

    @Test
    @DisplayName("Debe registrar presa en estado NORMAL con asimilación inicial de telemetría")
    void shouldRegisterDamInNormalState() {
        DamHydroTwinNode dam = service.registerDam(
                "ch-guadalquivir",
                "Presa de Iznajar",
                981.0, // 981 Hm3 max
                150.0,
                320.0,
                1200.0, // 1200 m3/s spillway
                450.0,  // 450 Hm3 actual (~45%)
                280.0
        );

        assertNotNull(dam.id());
        assertEquals(DamHydroTwinNode.DamSafetyStatus.NORMAL, dam.safetyStatus());
        assertEquals(981.0, dam.capacity().maxCapacityHm3());
    }

    @Test
    @DisplayName("Debe activar EMERGENCY_FLOOD_DISCHARGE si el caudal de entrada supera 500 m3/s (Avenida Torrencial)")
    void shouldTriggerEmergencyDischargeOnTorrentialInflow() {
        DamHydroTwinNode dam = service.registerDam(
                "ch-ebro",
                "Presa de Mequinenza",
                1530.0,
                200.0,
                124.0,
                3000.0,
                1300.0, // 85% lleno
                118.0
        );

        // Asimilación de avenida extrema: 850 m3/s de entrada
        DamHydroTwinNode updated = service.assimilateScadaTelemetry(dam.id(), 122.5, 850.0, 4.2);

        assertEquals(DamHydroTwinNode.DamSafetyStatus.EMERGENCY_FLOOD_DISCHARGE, updated.safetyStatus());
        assertEquals(3000.0, updated.currentState().spillwayDischargeM3s());
        assertEquals(1, updated.telemetryHistory().size());
    }
}
