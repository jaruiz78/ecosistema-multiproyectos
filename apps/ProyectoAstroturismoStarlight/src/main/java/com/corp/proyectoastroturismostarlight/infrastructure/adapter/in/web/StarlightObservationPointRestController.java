package com.corp.proyectoastroturismostarlight.infrastructure.adapter.in.web;

import com.corp.proyectoastroturismostarlight.domain.model.StarlightObservationPoint;
import com.corp.proyectoastroturismostarlight.domain.port.in.ManageStarlightObservationPointUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoastroturismostarlight")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class StarlightObservationPointRestController {

    private final ManageStarlightObservationPointUseCase useCase;

    public StarlightObservationPointRestController(ManageStarlightObservationPointUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<StarlightObservationPoint> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        StarlightObservationPoint created = useCase.createStarlightObservationPoint(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoastroturismostarlight/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StarlightObservationPoint> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findStarlightObservationPointById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
