package com.corp.proyectofemtosecondlaserprecision.infrastructure.adapter.in.web;

import com.corp.proyectofemtosecondlaserprecision.domain.model.LaserAblationPulseProfileToken;
import com.corp.proyectofemtosecondlaserprecision.domain.port.in.ManageLaserAblationPulseProfileTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectofemtosecondlaserprecision")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class LaserAblationPulseProfileTokenRestController {

    private final ManageLaserAblationPulseProfileTokenUseCase useCase;

    public LaserAblationPulseProfileTokenRestController(ManageLaserAblationPulseProfileTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LaserAblationPulseProfileToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LaserAblationPulseProfileToken created = useCase.createLaserAblationPulseProfileToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectofemtosecondlaserprecision/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaserAblationPulseProfileToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLaserAblationPulseProfileTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
