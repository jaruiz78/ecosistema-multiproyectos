package com.corp.proyectoturismotermalbalnearios.infrastructure.adapter.in.web;

import com.corp.proyectoturismotermalbalnearios.domain.model.TurismoTermalBalnearios;
import com.corp.proyectoturismotermalbalnearios.domain.port.in.ManageTurismoTermalBalneariosUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoturismotermalbalnearios")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class TurismoTermalBalneariosRestController {

    private final ManageTurismoTermalBalneariosUseCase useCase;

    public TurismoTermalBalneariosRestController(ManageTurismoTermalBalneariosUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TurismoTermalBalnearios> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TurismoTermalBalnearios created = useCase.createTurismoTermalBalnearios(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoturismotermalbalnearios/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurismoTermalBalnearios> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTurismoTermalBalneariosById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
