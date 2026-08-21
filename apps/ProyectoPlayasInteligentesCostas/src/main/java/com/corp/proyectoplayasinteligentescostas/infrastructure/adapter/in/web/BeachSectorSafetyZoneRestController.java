package com.corp.proyectoplayasinteligentescostas.infrastructure.adapter.in.web;

import com.corp.proyectoplayasinteligentescostas.domain.model.BeachSectorSafetyZone;
import com.corp.proyectoplayasinteligentescostas.domain.port.in.ManageBeachSectorSafetyZoneUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoplayasinteligentescostas")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BeachSectorSafetyZoneRestController {

    private final ManageBeachSectorSafetyZoneUseCase useCase;

    public BeachSectorSafetyZoneRestController(ManageBeachSectorSafetyZoneUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BeachSectorSafetyZone> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BeachSectorSafetyZone created = useCase.createBeachSectorSafetyZone(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoplayasinteligentescostas/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeachSectorSafetyZone> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBeachSectorSafetyZoneById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
