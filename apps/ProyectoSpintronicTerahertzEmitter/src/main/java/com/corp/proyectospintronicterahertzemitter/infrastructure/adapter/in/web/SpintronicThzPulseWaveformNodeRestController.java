package com.corp.proyectospintronicterahertzemitter.infrastructure.adapter.in.web;

import com.corp.proyectospintronicterahertzemitter.domain.model.SpintronicThzPulseWaveformNode;
import com.corp.proyectospintronicterahertzemitter.domain.port.in.ManageSpintronicThzPulseWaveformNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectospintronicterahertzemitter")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpintronicThzPulseWaveformNodeRestController {

    private final ManageSpintronicThzPulseWaveformNodeUseCase useCase;

    public SpintronicThzPulseWaveformNodeRestController(ManageSpintronicThzPulseWaveformNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpintronicThzPulseWaveformNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpintronicThzPulseWaveformNode created = useCase.createSpintronicThzPulseWaveformNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectospintronicterahertzemitter/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpintronicThzPulseWaveformNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpintronicThzPulseWaveformNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
