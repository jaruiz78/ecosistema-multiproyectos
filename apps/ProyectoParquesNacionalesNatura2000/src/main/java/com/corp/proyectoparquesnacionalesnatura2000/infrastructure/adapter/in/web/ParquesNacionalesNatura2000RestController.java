package com.corp.proyectoparquesnacionalesnatura2000.infrastructure.adapter.in.web;

import com.corp.proyectoparquesnacionalesnatura2000.domain.model.ParquesNacionalesNatura2000;
import com.corp.proyectoparquesnacionalesnatura2000.domain.port.in.ManageParquesNacionalesNatura2000UseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoparquesnacionalesnatura2000")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class ParquesNacionalesNatura2000RestController {

    private final ManageParquesNacionalesNatura2000UseCase useCase;

    public ParquesNacionalesNatura2000RestController(ManageParquesNacionalesNatura2000UseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ParquesNacionalesNatura2000> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ParquesNacionalesNatura2000 created = useCase.createParquesNacionalesNatura2000(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoparquesnacionalesnatura2000/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParquesNacionalesNatura2000> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findParquesNacionalesNatura2000ById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
