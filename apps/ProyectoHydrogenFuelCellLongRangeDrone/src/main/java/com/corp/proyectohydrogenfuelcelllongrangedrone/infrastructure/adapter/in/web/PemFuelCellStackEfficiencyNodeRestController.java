package com.corp.proyectohydrogenfuelcelllongrangedrone.infrastructure.adapter.in.web;

import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.model.PemFuelCellStackEfficiencyNode;
import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.port.in.ManagePemFuelCellStackEfficiencyNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohydrogenfuelcelllongrangedrone")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PemFuelCellStackEfficiencyNodeRestController {

    private final ManagePemFuelCellStackEfficiencyNodeUseCase useCase;

    public PemFuelCellStackEfficiencyNodeRestController(ManagePemFuelCellStackEfficiencyNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PemFuelCellStackEfficiencyNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PemFuelCellStackEfficiencyNode created = useCase.createPemFuelCellStackEfficiencyNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohydrogenfuelcelllongrangedrone/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PemFuelCellStackEfficiencyNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPemFuelCellStackEfficiencyNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
