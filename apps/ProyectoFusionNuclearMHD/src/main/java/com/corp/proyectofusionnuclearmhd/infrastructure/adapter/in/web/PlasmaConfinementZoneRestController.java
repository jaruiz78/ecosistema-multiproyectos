package com.corp.proyectofusionnuclearmhd.infrastructure.adapter.in.web;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import com.corp.proyectofusionnuclearmhd.domain.port.in.ManagePlasmaConfinementZoneUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectofusionnuclearmhd")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlasmaConfinementZoneRestController {

    private final ManagePlasmaConfinementZoneUseCase useCase;

    public PlasmaConfinementZoneRestController(ManagePlasmaConfinementZoneUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PlasmaConfinementZone> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PlasmaConfinementZone created = useCase.createPlasmaConfinementZone(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectofusionnuclearmhd/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlasmaConfinementZone> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPlasmaConfinementZoneById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
