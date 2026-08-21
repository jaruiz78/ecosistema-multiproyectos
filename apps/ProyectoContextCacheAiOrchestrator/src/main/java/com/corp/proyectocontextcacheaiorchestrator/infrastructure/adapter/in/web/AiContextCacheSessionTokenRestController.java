package com.corp.proyectocontextcacheaiorchestrator.infrastructure.adapter.in.web;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.in.ManageAiContextCacheSessionTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocontextcacheaiorchestrator")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AiContextCacheSessionTokenRestController {

    private final ManageAiContextCacheSessionTokenUseCase useCase;

    public AiContextCacheSessionTokenRestController(ManageAiContextCacheSessionTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AiContextCacheSessionToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AiContextCacheSessionToken created = useCase.createAiContextCacheSessionToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocontextcacheaiorchestrator/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiContextCacheSessionToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAiContextCacheSessionTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
