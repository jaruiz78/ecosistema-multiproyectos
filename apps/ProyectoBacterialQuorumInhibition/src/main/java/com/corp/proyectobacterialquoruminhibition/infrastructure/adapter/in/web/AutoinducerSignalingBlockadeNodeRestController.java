package com.corp.proyectobacterialquoruminhibition.infrastructure.adapter.in.web;

import com.corp.proyectobacterialquoruminhibition.domain.model.AutoinducerSignalingBlockadeNode;
import com.corp.proyectobacterialquoruminhibition.domain.port.in.ManageAutoinducerSignalingBlockadeNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobacterialquoruminhibition")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AutoinducerSignalingBlockadeNodeRestController {

    private final ManageAutoinducerSignalingBlockadeNodeUseCase useCase;

    public AutoinducerSignalingBlockadeNodeRestController(ManageAutoinducerSignalingBlockadeNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AutoinducerSignalingBlockadeNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AutoinducerSignalingBlockadeNode created = useCase.createAutoinducerSignalingBlockadeNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobacterialquoruminhibition/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoinducerSignalingBlockadeNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAutoinducerSignalingBlockadeNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
