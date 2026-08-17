package com.corp.proyectologistica.infrastructure.adapter.in.web;

import com.corp.proyectologistica.domain.model.Logistica;
import com.corp.proyectologistica.domain.port.in.ManageLogisticaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectologistica")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class LogisticaRestController {

    private final ManageLogisticaUseCase useCase;

    public LogisticaRestController(ManageLogisticaUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<Logistica> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        Logistica created = useCase.createLogistica(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectologistica/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Logistica> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLogisticaById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
