package com.corp.proyectominimalgenomechassisfoundry.infrastructure.adapter.in.web;

import com.corp.proyectominimalgenomechassisfoundry.domain.model.EssentialGeneSetCoverageToken;
import com.corp.proyectominimalgenomechassisfoundry.domain.port.in.ManageEssentialGeneSetCoverageTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectominimalgenomechassisfoundry")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class EssentialGeneSetCoverageTokenRestController {

    private final ManageEssentialGeneSetCoverageTokenUseCase useCase;

    public EssentialGeneSetCoverageTokenRestController(ManageEssentialGeneSetCoverageTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EssentialGeneSetCoverageToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EssentialGeneSetCoverageToken created = useCase.createEssentialGeneSetCoverageToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectominimalgenomechassisfoundry/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssentialGeneSetCoverageToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEssentialGeneSetCoverageTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
