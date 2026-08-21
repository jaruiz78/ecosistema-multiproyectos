package com.corp.proyectocellfreeproteinbiomanufacturing.infrastructure.adapter.in.web;

import com.corp.proyectocellfreeproteinbiomanufacturing.domain.model.CfpsReactionYieldBatchToken;
import com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.in.ManageCfpsReactionYieldBatchTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocellfreeproteinbiomanufacturing")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CfpsReactionYieldBatchTokenRestController {

    private final ManageCfpsReactionYieldBatchTokenUseCase useCase;

    public CfpsReactionYieldBatchTokenRestController(ManageCfpsReactionYieldBatchTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CfpsReactionYieldBatchToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CfpsReactionYieldBatchToken created = useCase.createCfpsReactionYieldBatchToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocellfreeproteinbiomanufacturing/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CfpsReactionYieldBatchToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCfpsReactionYieldBatchTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
