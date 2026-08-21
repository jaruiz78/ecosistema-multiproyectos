package com.corp.proyectomarinebioacousticssonar.infrastructure.adapter.in.web;

import com.corp.proyectomarinebioacousticssonar.domain.model.HydrophoneAcousticPulseNode;
import com.corp.proyectomarinebioacousticssonar.domain.port.in.ManageHydrophoneAcousticPulseNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomarinebioacousticssonar")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HydrophoneAcousticPulseNodeRestController {

    private final ManageHydrophoneAcousticPulseNodeUseCase useCase;

    public HydrophoneAcousticPulseNodeRestController(ManageHydrophoneAcousticPulseNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HydrophoneAcousticPulseNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HydrophoneAcousticPulseNode created = useCase.createHydrophoneAcousticPulseNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomarinebioacousticssonar/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HydrophoneAcousticPulseNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHydrophoneAcousticPulseNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
