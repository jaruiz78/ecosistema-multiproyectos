package com.corp.proyectohapticculturaltelepresence.infrastructure.adapter.in.web;

import com.corp.proyectohapticculturaltelepresence.domain.model.HapticExperienceStreamToken;
import com.corp.proyectohapticculturaltelepresence.domain.port.in.ManageHapticExperienceStreamTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohapticculturaltelepresence")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HapticExperienceStreamTokenRestController {

    private final ManageHapticExperienceStreamTokenUseCase useCase;

    public HapticExperienceStreamTokenRestController(ManageHapticExperienceStreamTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HapticExperienceStreamToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HapticExperienceStreamToken created = useCase.createHapticExperienceStreamToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohapticculturaltelepresence/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HapticExperienceStreamToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHapticExperienceStreamTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
