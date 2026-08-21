package com.corp.proyectozeroknowledgecreditrating.infrastructure.adapter.in.web;

import com.corp.proyectozeroknowledgecreditrating.domain.model.ZkCreditEligibilityProofToken;
import com.corp.proyectozeroknowledgecreditrating.domain.port.in.ManageZkCreditEligibilityProofTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectozeroknowledgecreditrating")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ZkCreditEligibilityProofTokenRestController {

    private final ManageZkCreditEligibilityProofTokenUseCase useCase;

    public ZkCreditEligibilityProofTokenRestController(ManageZkCreditEligibilityProofTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ZkCreditEligibilityProofToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ZkCreditEligibilityProofToken created = useCase.createZkCreditEligibilityProofToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectozeroknowledgecreditrating/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZkCreditEligibilityProofToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findZkCreditEligibilityProofTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
