package com.corp.proyectoautonomousmaglevfreight.infrastructure.adapter.in.web;

import com.corp.proyectoautonomousmaglevfreight.domain.model.MaglevFreightTrainTrackNode;
import com.corp.proyectoautonomousmaglevfreight.domain.port.in.ManageMaglevFreightTrainTrackNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoautonomousmaglevfreight")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MaglevFreightTrainTrackNodeRestController {

    private final ManageMaglevFreightTrainTrackNodeUseCase useCase;

    public MaglevFreightTrainTrackNodeRestController(ManageMaglevFreightTrainTrackNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MaglevFreightTrainTrackNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MaglevFreightTrainTrackNode created = useCase.createMaglevFreightTrainTrackNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoautonomousmaglevfreight/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaglevFreightTrainTrackNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMaglevFreightTrainTrackNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
