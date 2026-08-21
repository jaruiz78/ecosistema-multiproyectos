package com.corp.proyectocrosschainassetsettlement.infrastructure.adapter.in.web;

import com.corp.proyectocrosschainassetsettlement.domain.model.HtlcAtomicSwapEscrowLockToken;
import com.corp.proyectocrosschainassetsettlement.domain.port.in.ManageHtlcAtomicSwapEscrowLockTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocrosschainassetsettlement")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HtlcAtomicSwapEscrowLockTokenRestController {

    private final ManageHtlcAtomicSwapEscrowLockTokenUseCase useCase;

    public HtlcAtomicSwapEscrowLockTokenRestController(ManageHtlcAtomicSwapEscrowLockTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HtlcAtomicSwapEscrowLockToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HtlcAtomicSwapEscrowLockToken created = useCase.createHtlcAtomicSwapEscrowLockToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocrosschainassetsettlement/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HtlcAtomicSwapEscrowLockToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHtlcAtomicSwapEscrowLockTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
