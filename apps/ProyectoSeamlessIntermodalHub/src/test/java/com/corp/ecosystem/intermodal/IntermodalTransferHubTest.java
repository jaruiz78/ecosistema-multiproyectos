package com.corp.ecosystem.intermodal;

import com.corp.ecosystem.intermodal.application.IntermodalHubService;
import com.corp.ecosystem.intermodal.domain.IntermodalTransferHub;
import com.corp.ecosystem.intermodal.domain.port.IntermodalHubRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoSeamlessIntermodalHub.
 */
class IntermodalTransferHubTest {

    static class InMemoryHubRepository implements IntermodalHubRepositoryPort {
        private final Map<IntermodalTransferHub.HubId, IntermodalTransferHub> storage = new ConcurrentHashMap<>();

        @Override
        public IntermodalTransferHub save(IntermodalTransferHub hub) {
            storage.put(hub.id(), hub);
            return hub;
        }

        @Override
        public Optional<IntermodalTransferHub> findById(IntermodalTransferHub.HubId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryHubRepository repository = new InMemoryHubRepository();
    private final IntermodalHubService service = new IntermodalHubService(repository);

    @Test
    @DisplayName("Debe registrar terminal portuaria con capacidad de flota agregada")
    void shouldRegisterCruiseTerminalWithFleet() {
        IntermodalTransferHub hub = service.registerTerminalHub(
                "port-barcelona",
                "Terminal de Cruceros Adosat A",
                IntermodalTransferHub.HubType.CRUISE_PORT_TERMINAL,
                15, // 15 minibuses (240 pax)
                20, // 20 vans (160 pax)
                50  // 50 taxis (200 pax) -> Total: 600 pax
        );

        assertNotNull(hub.id());
        assertEquals(IntermodalTransferHub.HubType.CRUISE_PORT_TERMINAL, hub.type());
        assertEquals(600, hub.fleet().totalCapacityPax());
    }

    @Test
    @DisplayName("Debe despachar grupo en MINIBUS_16 cuando el lote de pasajeros hacia una celda H3 supera los 8")
    void shouldDispatchMinibusWhenPaxExceeds8() {
        IntermodalTransferHub hub = service.registerTerminalHub(
                "airport-palma",
                "Aeropuerto Son Sant Joan - T3",
                IntermodalTransferHub.HubType.AIRPORT_INTERNATIONAL,
                10, 10, 20
        );

        // Despacho de 14 turistas con destino al cluster hotelero de Magaluf (celda H3)
        IntermodalTransferHub updated = service.dispatchTransfers(
                hub.id(),
                "FLIGHT-LH1820",
                0x88390cb307fffffL,
                "Hoteles Calvia / Magaluf",
                14
        );

        assertEquals(1, updated.activeDispatches().size());
        assertEquals("MINIBUS_16", updated.activeDispatches().getFirst().assignedVehicleType());
        assertEquals(IntermodalTransferHub.DispatchStatus.BOARDING, updated.activeDispatches().getFirst().status());
    }
}
