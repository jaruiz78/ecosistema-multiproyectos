package com.corp.proyectohypersonicintercontinentalfreight.infrastructure.adapter.in.web;

import com.corp.proyectohypersonicintercontinentalfreight.domain.model.ScramjetCombustionPressureRatioNode;
import com.corp.proyectohypersonicintercontinentalfreight.domain.port.in.ManageScramjetCombustionPressureRatioNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohypersonicintercontinentalfreight")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ScramjetCombustionPressureRatioNodeRestController {

    private final ManageScramjetCombustionPressureRatioNodeUseCase useCase;

    public ScramjetCombustionPressureRatioNodeRestController(ManageScramjetCombustionPressureRatioNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ScramjetCombustionPressureRatioNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ScramjetCombustionPressureRatioNode created = useCase.createScramjetCombustionPressureRatioNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohypersonicintercontinentalfreight/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScramjetCombustionPressureRatioNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findScramjetCombustionPressureRatioNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
