package com.corp.proyectosuborbitalspacetourismtwin.infrastructure.adapter.in.web;

import com.corp.proyectosuborbitalspacetourismtwin.domain.model.SpaceTourismPassengerGForceNode;
import com.corp.proyectosuborbitalspacetourismtwin.domain.port.in.ManageSpaceTourismPassengerGForceNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosuborbitalspacetourismtwin")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpaceTourismPassengerGForceNodeRestController {

    private final ManageSpaceTourismPassengerGForceNodeUseCase useCase;

    public SpaceTourismPassengerGForceNodeRestController(ManageSpaceTourismPassengerGForceNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpaceTourismPassengerGForceNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpaceTourismPassengerGForceNode created = useCase.createSpaceTourismPassengerGForceNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosuborbitalspacetourismtwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceTourismPassengerGForceNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpaceTourismPassengerGForceNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
