package com.corp.proyectostratosphericaerosollidarnetwork.infrastructure.adapter.in.web;

import com.corp.proyectostratosphericaerosollidarnetwork.domain.model.AerosolBackscatterExtinctionProfileNode;
import com.corp.proyectostratosphericaerosollidarnetwork.domain.port.in.ManageAerosolBackscatterExtinctionProfileNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectostratosphericaerosollidarnetwork")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AerosolBackscatterExtinctionProfileNodeRestController {

    private final ManageAerosolBackscatterExtinctionProfileNodeUseCase useCase;

    public AerosolBackscatterExtinctionProfileNodeRestController(ManageAerosolBackscatterExtinctionProfileNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AerosolBackscatterExtinctionProfileNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AerosolBackscatterExtinctionProfileNode created = useCase.createAerosolBackscatterExtinctionProfileNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectostratosphericaerosollidarnetwork/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AerosolBackscatterExtinctionProfileNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAerosolBackscatterExtinctionProfileNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
