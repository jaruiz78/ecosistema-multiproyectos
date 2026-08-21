package com.corp.proyectobacteriophageprecisionantimicrobial.infrastructure.adapter.in.web;

import com.corp.proyectobacteriophageprecisionantimicrobial.domain.model.EndolysinLyticActivityScoreToken;
import com.corp.proyectobacteriophageprecisionantimicrobial.domain.port.in.ManageEndolysinLyticActivityScoreTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobacteriophageprecisionantimicrobial")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class EndolysinLyticActivityScoreTokenRestController {

    private final ManageEndolysinLyticActivityScoreTokenUseCase useCase;

    public EndolysinLyticActivityScoreTokenRestController(ManageEndolysinLyticActivityScoreTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EndolysinLyticActivityScoreToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EndolysinLyticActivityScoreToken created = useCase.createEndolysinLyticActivityScoreToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobacteriophageprecisionantimicrobial/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndolysinLyticActivityScoreToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEndolysinLyticActivityScoreTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
