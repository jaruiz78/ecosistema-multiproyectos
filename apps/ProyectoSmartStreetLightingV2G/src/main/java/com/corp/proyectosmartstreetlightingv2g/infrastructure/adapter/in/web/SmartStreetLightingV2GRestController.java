package com.corp.proyectosmartstreetlightingv2g.infrastructure.adapter.in.web;

import com.corp.proyectosmartstreetlightingv2g.domain.model.SmartStreetLightingV2G;
import com.corp.proyectosmartstreetlightingv2g.domain.port.in.ManageSmartStreetLightingV2GUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosmartstreetlightingv2g")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class SmartStreetLightingV2GRestController {

    private final ManageSmartStreetLightingV2GUseCase useCase;

    public SmartStreetLightingV2GRestController(ManageSmartStreetLightingV2GUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SmartStreetLightingV2G> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SmartStreetLightingV2G created = useCase.createSmartStreetLightingV2G(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosmartstreetlightingv2g/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmartStreetLightingV2G> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSmartStreetLightingV2GById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
