package com.corp.proyectosoilbiocarbontwin.infrastructure.adapter.in.web;

import com.corp.proyectosoilbiocarbontwin.domain.model.SoilBioCarbonTwin;
import com.corp.proyectosoilbiocarbontwin.domain.port.in.ManageSoilBioCarbonTwinUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosoilbiocarbontwin")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class SoilBioCarbonTwinRestController {

    private final ManageSoilBioCarbonTwinUseCase useCase;

    public SoilBioCarbonTwinRestController(ManageSoilBioCarbonTwinUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SoilBioCarbonTwin> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SoilBioCarbonTwin created = useCase.createSoilBioCarbonTwin(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosoilbiocarbontwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoilBioCarbonTwin> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSoilBioCarbonTwinById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
