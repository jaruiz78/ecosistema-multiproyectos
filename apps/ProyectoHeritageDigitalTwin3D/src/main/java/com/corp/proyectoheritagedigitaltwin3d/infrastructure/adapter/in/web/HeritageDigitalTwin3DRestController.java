package com.corp.proyectoheritagedigitaltwin3d.infrastructure.adapter.in.web;

import com.corp.proyectoheritagedigitaltwin3d.domain.model.HeritageDigitalTwin3D;
import com.corp.proyectoheritagedigitaltwin3d.domain.port.in.ManageHeritageDigitalTwin3DUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoheritagedigitaltwin3d")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class HeritageDigitalTwin3DRestController {

    private final ManageHeritageDigitalTwin3DUseCase useCase;

    public HeritageDigitalTwin3DRestController(ManageHeritageDigitalTwin3DUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HeritageDigitalTwin3D> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HeritageDigitalTwin3D created = useCase.createHeritageDigitalTwin3D(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoheritagedigitaltwin3d/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeritageDigitalTwin3D> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHeritageDigitalTwin3DById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
