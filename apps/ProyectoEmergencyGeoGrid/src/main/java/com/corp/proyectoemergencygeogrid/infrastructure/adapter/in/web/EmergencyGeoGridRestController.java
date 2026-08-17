package com.corp.proyectoemergencygeogrid.infrastructure.adapter.in.web;

import com.corp.proyectoemergencygeogrid.domain.model.EmergencyGeoGrid;
import com.corp.proyectoemergencygeogrid.domain.port.in.ManageEmergencyGeoGridUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoemergencygeogrid")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class EmergencyGeoGridRestController {

    private final ManageEmergencyGeoGridUseCase useCase;

    public EmergencyGeoGridRestController(ManageEmergencyGeoGridUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EmergencyGeoGrid> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EmergencyGeoGrid created = useCase.createEmergencyGeoGrid(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoemergencygeogrid/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyGeoGrid> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEmergencyGeoGridById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
