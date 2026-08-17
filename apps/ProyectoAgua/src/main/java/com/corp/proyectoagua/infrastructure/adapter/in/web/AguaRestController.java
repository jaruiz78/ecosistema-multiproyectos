package com.corp.proyectoagua.infrastructure.adapter.in.web;

import com.corp.proyectoagua.domain.model.Agua;
import com.corp.proyectoagua.domain.port.in.ManageAguaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoagua")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class AguaRestController {

    private final ManageAguaUseCase useCase;

    public AguaRestController(ManageAguaUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<Agua> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        Agua created = useCase.createAgua(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoagua/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agua> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAguaById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
