package com.corp.proyectoquantumdottandemsolar.infrastructure.adapter.in.web;

import com.corp.proyectoquantumdottandemsolar.domain.model.QuantumDotSolarCellBatch;
import com.corp.proyectoquantumdottandemsolar.domain.port.in.ManageQuantumDotSolarCellBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumdottandemsolar")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuantumDotSolarCellBatchRestController {

    private final ManageQuantumDotSolarCellBatchUseCase useCase;

    public QuantumDotSolarCellBatchRestController(ManageQuantumDotSolarCellBatchUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuantumDotSolarCellBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuantumDotSolarCellBatch created = useCase.createQuantumDotSolarCellBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumdottandemsolar/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuantumDotSolarCellBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuantumDotSolarCellBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
