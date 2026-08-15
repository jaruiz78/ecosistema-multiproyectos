package com.corp.ecosystem.zerotrustot;

import com.corp.ecosystem.zerotrustot.application.ZeroTrustOtMeshService;
import com.corp.ecosystem.zerotrustot.domain.ScadaNodeSecurityTwin;
import com.corp.ecosystem.zerotrustot.domain.port.ScadaSecurityRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoZeroTrustOTMesh.
 */
class ScadaNodeSecurityTwinTest {

    static class InMemoryScadaSecurityRepository implements ScadaSecurityRepositoryPort {
        private final Map<ScadaNodeSecurityTwin.NodeSecurityId, ScadaNodeSecurityTwin> storage = new ConcurrentHashMap<>();

        @Override
        public ScadaNodeSecurityTwin save(ScadaNodeSecurityTwin node) {
            storage.put(node.id(), node);
            return node;
        }

        @Override
        public Optional<ScadaNodeSecurityTwin> findById(ScadaNodeSecurityTwin.NodeSecurityId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryScadaSecurityRepository repository = new InMemoryScadaSecurityRepository();
    private final ZeroTrustOtMeshService service = new ZeroTrustOtMeshService(repository);

    @Test
    @DisplayName("Debe permitir comando Modbus legítimo dentro de los rangos de presión física")
    void shouldAllowLegitimateModbusCommand() {
        ScadaNodeSecurityTwin node = service.registerScadaNode(
                "ch-ebro",
                "192.168.10.45:502/RTU1",
                12.0, // 12 bar max
                50.0
        );

        ScadaNodeSecurityTwin updated = service.inspectAndFilterCommand(node.id(), "SET_VALVE_PRESSURE", 8.5, 7.0);

        assertEquals(ScadaNodeSecurityTwin.SecurityDefenseStatus.TRUSTED_SECURE, updated.defenseStatus());
        assertTrue(updated.lastCommand().isPhysicallyFeasible());
    }

    @Test
    @DisplayName("Debe interceptar y bloquear comando que viole el umbral físico (Ataque OT)")
    void shouldBlockCommandViolatingPhysicalThreshold() {
        ScadaNodeSecurityTwin node = service.registerScadaNode(
                "red-electrica-substation",
                "10.0.4.12:502/PLC2",
                10.0,
                100.0
        );

        // Intento de forzado a 25 bar (> 10 bar)
        ScadaNodeSecurityTwin updated = service.inspectAndFilterCommand(node.id(), "FORCE_OVERPRESSURE", 25.0, 9.8);

        assertEquals(ScadaNodeSecurityTwin.SecurityDefenseStatus.INTRUSION_BLOCKED_PHYSICAL_DISCREPANCY, updated.defenseStatus());
        assertFalse(updated.lastCommand().isPhysicallyFeasible());
    }
}
