package com.corp.proyectogreenhydrogendesal.infrastructure.adapter.in.web;

import com.corp.proyectogreenhydrogendesal.domain.model.GreenHydrogenDesal;
import com.corp.proyectogreenhydrogendesal.domain.port.in.ManageGreenHydrogenDesalUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectogreenhydrogendesal")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class GreenHydrogenDesalRestController {

    private final ManageGreenHydrogenDesalUseCase useCase;

    public GreenHydrogenDesalRestController(ManageGreenHydrogenDesalUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GreenHydrogenDesal> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GreenHydrogenDesal created = useCase.createGreenHydrogenDesal(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectogreenhydrogendesal/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GreenHydrogenDesal> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGreenHydrogenDesalById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
