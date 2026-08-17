package com.corp.proyectointerplanetaryswarmmesh.application;

import com.corp.proyectointerplanetaryswarmmesh.application.service.InterplanetarySwarmRoutingService;
import com.corp.proyectointerplanetaryswarmmesh.infrastructure.adapter.InMemoryDtnPacketRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterplanetarySwarmRoutingServiceTest {

    @Test
    @DisplayName("Debe despachar paquete DTN Bundle Protocol y persistir en repositorio")
    void testDispatchBundle() {
        var repo = new InMemoryDtnPacketRepositoryAdapter();
        var service = new InterplanetarySwarmRoutingService(repo);

        var packet = service.dispatchBundle("BUNDLE-MARS-RELAY-01", "dtn://earth-ground.station/1", "dtn://mars-orbiter.relay/2", 86400, 256);

        assertNotNull(packet);
        assertEquals("BUNDLE-MARS-RELAY-01", packet.bundleId());
        assertTrue(packet.custodyTransferRequested());
        assertEquals(256, packet.payloadSizeKb());

        assertTrue(repo.findById("BUNDLE-MARS-RELAY-01").isPresent());
    }
}
