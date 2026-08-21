package com.corp.proyectodesertdustairqualitygrid.infrastructure.adapter.in.web;

import com.corp.proyectodesertdustairqualitygrid.domain.model.MineralDustAerosolOpticalDepthNode;
import com.corp.proyectodesertdustairqualitygrid.domain.port.in.ManageMineralDustAerosolOpticalDepthNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodesertdustairqualitygrid")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MineralDustAerosolOpticalDepthNodeRestController {

    private final ManageMineralDustAerosolOpticalDepthNodeUseCase useCase;

    public MineralDustAerosolOpticalDepthNodeRestController(ManageMineralDustAerosolOpticalDepthNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MineralDustAerosolOpticalDepthNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MineralDustAerosolOpticalDepthNode created = useCase.createMineralDustAerosolOpticalDepthNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodesertdustairqualitygrid/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MineralDustAerosolOpticalDepthNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMineralDustAerosolOpticalDepthNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
