package com.corp.proyectotokenrwa.infrastructure.adapter.in.web;

import com.corp.proyectotokenrwa.domain.model.TokenRWA;
import com.corp.proyectotokenrwa.domain.port.in.ManageTokenRWAUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectotokenrwa")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_9_fintech_facturacion_stripe_sagas">FACULTAD_X: Fintech, Stripe Connect, Sagas & Escrow</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class TokenRWARestController {

    private final ManageTokenRWAUseCase useCase;

    public TokenRWARestController(ManageTokenRWAUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TokenRWA> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TokenRWA created = useCase.createTokenRWA(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectotokenrwa/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenRWA> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTokenRWAById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
