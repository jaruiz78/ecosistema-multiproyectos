package com.corp.proyectosmartwaterdesal.infrastructure.adapter.in.web;

import com.corp.proyectosmartwaterdesal.domain.model.SmartWaterDesal;
import com.corp.proyectosmartwaterdesal.domain.port.in.ManageSmartWaterDesalUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosmartwaterdesal")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class SmartWaterDesalRestController {

    private final ManageSmartWaterDesalUseCase useCase;

    public SmartWaterDesalRestController(ManageSmartWaterDesalUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SmartWaterDesal> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SmartWaterDesal created = useCase.createSmartWaterDesal(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosmartwaterdesal/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmartWaterDesal> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSmartWaterDesalById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
