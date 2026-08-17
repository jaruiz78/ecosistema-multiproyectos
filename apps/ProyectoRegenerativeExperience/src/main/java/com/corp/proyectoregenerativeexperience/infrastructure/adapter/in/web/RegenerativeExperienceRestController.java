package com.corp.proyectoregenerativeexperience.infrastructure.adapter.in.web;

import com.corp.proyectoregenerativeexperience.domain.model.RegenerativeExperience;
import com.corp.proyectoregenerativeexperience.domain.port.in.ManageRegenerativeExperienceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoregenerativeexperience")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class RegenerativeExperienceRestController {

    private final ManageRegenerativeExperienceUseCase useCase;

    public RegenerativeExperienceRestController(ManageRegenerativeExperienceUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<RegenerativeExperience> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        RegenerativeExperience created = useCase.createRegenerativeExperience(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoregenerativeexperience/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegenerativeExperience> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findRegenerativeExperienceById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
