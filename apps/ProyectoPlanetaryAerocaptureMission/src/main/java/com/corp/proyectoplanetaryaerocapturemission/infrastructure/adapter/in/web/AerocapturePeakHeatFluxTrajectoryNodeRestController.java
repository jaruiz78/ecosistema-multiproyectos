package com.corp.proyectoplanetaryaerocapturemission.infrastructure.adapter.in.web;

import com.corp.proyectoplanetaryaerocapturemission.domain.model.AerocapturePeakHeatFluxTrajectoryNode;
import com.corp.proyectoplanetaryaerocapturemission.domain.port.in.ManageAerocapturePeakHeatFluxTrajectoryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoplanetaryaerocapturemission")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AerocapturePeakHeatFluxTrajectoryNodeRestController {

    private final ManageAerocapturePeakHeatFluxTrajectoryNodeUseCase useCase;

    public AerocapturePeakHeatFluxTrajectoryNodeRestController(ManageAerocapturePeakHeatFluxTrajectoryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AerocapturePeakHeatFluxTrajectoryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AerocapturePeakHeatFluxTrajectoryNode created = useCase.createAerocapturePeakHeatFluxTrajectoryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoplanetaryaerocapturemission/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AerocapturePeakHeatFluxTrajectoryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAerocapturePeakHeatFluxTrajectoryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
