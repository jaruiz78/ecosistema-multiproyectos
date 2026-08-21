package com.corp.proyectomaritimeautonomousfleet.infrastructure.adapter.in.web;

import com.corp.proyectomaritimeautonomousfleet.domain.model.AutonomousVesselVoyage;
import com.corp.proyectomaritimeautonomousfleet.domain.port.in.ManageAutonomousVesselVoyageUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomaritimeautonomousfleet")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AutonomousVesselVoyageRestController {

    private final ManageAutonomousVesselVoyageUseCase useCase;

    public AutonomousVesselVoyageRestController(ManageAutonomousVesselVoyageUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AutonomousVesselVoyage> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AutonomousVesselVoyage created = useCase.createAutonomousVesselVoyage(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomaritimeautonomousfleet/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutonomousVesselVoyage> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAutonomousVesselVoyageById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
