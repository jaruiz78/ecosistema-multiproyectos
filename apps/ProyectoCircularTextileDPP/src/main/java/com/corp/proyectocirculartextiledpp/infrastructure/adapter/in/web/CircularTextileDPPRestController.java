package com.corp.proyectocirculartextiledpp.infrastructure.adapter.in.web;

import com.corp.proyectocirculartextiledpp.domain.model.CircularTextileDPP;
import com.corp.proyectocirculartextiledpp.domain.port.in.ManageCircularTextileDPPUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocirculartextiledpp")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CircularTextileDPPRestController {

    private final ManageCircularTextileDPPUseCase useCase;

    public CircularTextileDPPRestController(ManageCircularTextileDPPUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CircularTextileDPP> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CircularTextileDPP created = useCase.createCircularTextileDPP(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocirculartextiledpp/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CircularTextileDPP> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCircularTextileDPPById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
