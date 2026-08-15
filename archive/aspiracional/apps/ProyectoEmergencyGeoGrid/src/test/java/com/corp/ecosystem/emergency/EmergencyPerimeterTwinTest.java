package com.corp.ecosystem.emergency;

import com.corp.ecosystem.emergency.application.EmergencyGeoGridService;
import com.corp.ecosystem.emergency.domain.EmergencyPerimeterTwin;
import com.corp.ecosystem.emergency.domain.port.EmergencyTwinRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoEmergencyGeoGrid.
 */
class EmergencyPerimeterTwinTest {

    static class InMemoryEmergencyRepository implements EmergencyTwinRepositoryPort {
        private final Map<EmergencyPerimeterTwin.EmergencyId, EmergencyPerimeterTwin> storage = new ConcurrentHashMap<>();

        @Override
        public EmergencyPerimeterTwin save(EmergencyPerimeterTwin emergency) {
            storage.put(emergency.id(), emergency);
            return emergency;
        }

        @Override
        public Optional<EmergencyPerimeterTwin> findById(EmergencyPerimeterTwin.EmergencyId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryEmergencyRepository repository = new InMemoryEmergencyRepository();
    private final EmergencyGeoGridService service = new EmergencyGeoGridService(repository);

    @Test
    @DisplayName("Debe declarar conato de incendio en SITUATION_1_LOCAL")
    void shouldDeclareWildfireInLocalState() {
        EmergencyPerimeterTwin emergency = service.declareEmergency(
                "proteccion-civil-andalucia",
                EmergencyPerimeterTwin.EmergencyType.WILDFIRE_FOREST,
                0x88390cb307fffffL,
                15.0, // 15 km/h viento
                180.0,
                34.0
        );

        assertNotNull(emergency.id());
        assertEquals(EmergencyPerimeterTwin.EmergencyLevel.SITUATION_1_LOCAL, emergency.level());
        assertEquals(1, emergency.activeH3CellsRes8().size());
        assertFalse(emergency.evacuation().isAirSupportRequested());
    }

    @Test
    @DisplayName("Debe escalar a SITUATION_3_NATIONAL_UME y solicitar apoyo aéreo con vientos > 50 km/h y múltiples celdas")
    void shouldEscalateToSituation3UmeOnExtremeSpread() {
        EmergencyPerimeterTwin emergency = service.declareEmergency(
                "proteccion-civil-canarias",
                EmergencyPerimeterTwin.EmergencyType.WILDFIRE_FOREST,
                0x88390cb307fffffL,
                20.0,
                45.0,
                38.0
        );

        List<Long> newlyIgnitedCells = List.of(
                0x88390cb301fffffL, 0x88390cb302fffffL, 0x88390cb303fffffL,
                0x88390cb304fffffL, 0x88390cb305fffffL, 0x88390cb306fffffL
        );

        // Viento extremo de 65 km/h
        EmergencyPerimeterTwin updated = service.updateFireSpread(emergency.id(), 65.0, 50.0, newlyIgnitedCells);

        assertEquals(EmergencyPerimeterTwin.EmergencyLevel.SITUATION_3_NATIONAL_UME, updated.level());
        assertTrue(updated.evacuation().isAirSupportRequested());
        assertEquals(7, updated.activeH3CellsRes8().size());
    }
}
