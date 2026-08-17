package com.corp.proyectodefensa.infrastructure.adapter.in.web;

import com.corp.proyectodefensa.domain.model.Defensa;
import com.corp.proyectodefensa.domain.port.in.ManageDefensaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodefensa")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_10_identidad_soberana_privacidad_zkp">FACULTAD_XI: Identidad Soberana & Zero-Trust BeyondCorp</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class DefensaRestController {

    private final ManageDefensaUseCase useCase;

    public DefensaRestController(ManageDefensaUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<Defensa> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        Defensa created = useCase.createDefensa(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodefensa/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Defensa> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDefensaById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
