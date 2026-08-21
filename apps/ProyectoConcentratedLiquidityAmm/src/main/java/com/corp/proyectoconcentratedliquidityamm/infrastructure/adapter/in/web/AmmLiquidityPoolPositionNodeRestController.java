package com.corp.proyectoconcentratedliquidityamm.infrastructure.adapter.in.web;

import com.corp.proyectoconcentratedliquidityamm.domain.model.AmmLiquidityPoolPositionNode;
import com.corp.proyectoconcentratedliquidityamm.domain.port.in.ManageAmmLiquidityPoolPositionNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoconcentratedliquidityamm")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AmmLiquidityPoolPositionNodeRestController {

    private final ManageAmmLiquidityPoolPositionNodeUseCase useCase;

    public AmmLiquidityPoolPositionNodeRestController(ManageAmmLiquidityPoolPositionNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AmmLiquidityPoolPositionNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AmmLiquidityPoolPositionNode created = useCase.createAmmLiquidityPoolPositionNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoconcentratedliquidityamm/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmmLiquidityPoolPositionNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAmmLiquidityPoolPositionNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
