package com.corp.proyectocircular.infrastructure.adapter.in.web;

import com.corp.proyectocircular.domain.model.Circular;
import com.corp.proyectocircular.domain.port.in.ManageCircularUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocircular")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CircularRestController {

    private final ManageCircularUseCase useCase;

    public CircularRestController(ManageCircularUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<Circular> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        Circular created = useCase.createCircular(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocircular/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Circular> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCircularById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
