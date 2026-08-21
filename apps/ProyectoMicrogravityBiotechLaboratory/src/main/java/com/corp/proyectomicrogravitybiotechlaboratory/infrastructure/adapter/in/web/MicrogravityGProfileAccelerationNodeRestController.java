package com.corp.proyectomicrogravitybiotechlaboratory.infrastructure.adapter.in.web;

import com.corp.proyectomicrogravitybiotechlaboratory.domain.model.MicrogravityGProfileAccelerationNode;
import com.corp.proyectomicrogravitybiotechlaboratory.domain.port.in.ManageMicrogravityGProfileAccelerationNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomicrogravitybiotechlaboratory")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MicrogravityGProfileAccelerationNodeRestController {

    private final ManageMicrogravityGProfileAccelerationNodeUseCase useCase;

    public MicrogravityGProfileAccelerationNodeRestController(ManageMicrogravityGProfileAccelerationNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MicrogravityGProfileAccelerationNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MicrogravityGProfileAccelerationNode created = useCase.createMicrogravityGProfileAccelerationNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomicrogravitybiotechlaboratory/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MicrogravityGProfileAccelerationNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMicrogravityGProfileAccelerationNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
