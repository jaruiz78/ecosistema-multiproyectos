package com.corp.proyectoprogrammableofflinecbdc.infrastructure.adapter.in.web;

import com.corp.proyectoprogrammableofflinecbdc.domain.model.OfflineCbdcSpendProofToken;
import com.corp.proyectoprogrammableofflinecbdc.domain.port.in.ManageOfflineCbdcSpendProofTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoprogrammableofflinecbdc")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class OfflineCbdcSpendProofTokenRestController {

    private final ManageOfflineCbdcSpendProofTokenUseCase useCase;

    public OfflineCbdcSpendProofTokenRestController(ManageOfflineCbdcSpendProofTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<OfflineCbdcSpendProofToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        OfflineCbdcSpendProofToken created = useCase.createOfflineCbdcSpendProofToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoprogrammableofflinecbdc/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfflineCbdcSpendProofToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findOfflineCbdcSpendProofTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
