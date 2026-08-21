package com.corp.proyectosolidstatebatterystorage.infrastructure.adapter.in.web;

import com.corp.proyectosolidstatebatterystorage.domain.model.SolidStateElectrolyteCellBatch;
import com.corp.proyectosolidstatebatterystorage.domain.port.in.ManageSolidStateElectrolyteCellBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosolidstatebatterystorage")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SolidStateElectrolyteCellBatchRestController {

    private final ManageSolidStateElectrolyteCellBatchUseCase useCase;

    public SolidStateElectrolyteCellBatchRestController(ManageSolidStateElectrolyteCellBatchUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SolidStateElectrolyteCellBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SolidStateElectrolyteCellBatch created = useCase.createSolidStateElectrolyteCellBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosolidstatebatterystorage/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolidStateElectrolyteCellBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSolidStateElectrolyteCellBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
