package com.corp.proyectoconfidentialdatacleanroom.infrastructure.adapter.in.web;

import com.corp.proyectoconfidentialdatacleanroom.domain.model.SecureEnclaveAnalyticsAttestationToken;
import com.corp.proyectoconfidentialdatacleanroom.domain.port.in.ManageSecureEnclaveAnalyticsAttestationTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoconfidentialdatacleanroom")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SecureEnclaveAnalyticsAttestationTokenRestController {

    private final ManageSecureEnclaveAnalyticsAttestationTokenUseCase useCase;

    public SecureEnclaveAnalyticsAttestationTokenRestController(ManageSecureEnclaveAnalyticsAttestationTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SecureEnclaveAnalyticsAttestationToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SecureEnclaveAnalyticsAttestationToken created = useCase.createSecureEnclaveAnalyticsAttestationToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoconfidentialdatacleanroom/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecureEnclaveAnalyticsAttestationToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSecureEnclaveAnalyticsAttestationTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
