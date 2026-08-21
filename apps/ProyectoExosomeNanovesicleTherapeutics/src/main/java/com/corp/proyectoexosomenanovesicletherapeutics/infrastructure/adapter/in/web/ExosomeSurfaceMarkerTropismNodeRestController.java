package com.corp.proyectoexosomenanovesicletherapeutics.infrastructure.adapter.in.web;

import com.corp.proyectoexosomenanovesicletherapeutics.domain.model.ExosomeSurfaceMarkerTropismNode;
import com.corp.proyectoexosomenanovesicletherapeutics.domain.port.in.ManageExosomeSurfaceMarkerTropismNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoexosomenanovesicletherapeutics")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ExosomeSurfaceMarkerTropismNodeRestController {

    private final ManageExosomeSurfaceMarkerTropismNodeUseCase useCase;

    public ExosomeSurfaceMarkerTropismNodeRestController(ManageExosomeSurfaceMarkerTropismNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ExosomeSurfaceMarkerTropismNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ExosomeSurfaceMarkerTropismNode created = useCase.createExosomeSurfaceMarkerTropismNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoexosomenanovesicletherapeutics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExosomeSurfaceMarkerTropismNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findExosomeSurfaceMarkerTropismNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
