package com.corp.proyectosubseafiberseismicmonitor.infrastructure.adapter.in.web;

import com.corp.proyectosubseafiberseismicmonitor.domain.model.FiberOpticRayleighAcousticNode;
import com.corp.proyectosubseafiberseismicmonitor.domain.port.in.ManageFiberOpticRayleighAcousticNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosubseafiberseismicmonitor")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class FiberOpticRayleighAcousticNodeRestController {

    private final ManageFiberOpticRayleighAcousticNodeUseCase useCase;

    public FiberOpticRayleighAcousticNodeRestController(ManageFiberOpticRayleighAcousticNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<FiberOpticRayleighAcousticNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        FiberOpticRayleighAcousticNode created = useCase.createFiberOpticRayleighAcousticNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosubseafiberseismicmonitor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiberOpticRayleighAcousticNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findFiberOpticRayleighAcousticNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
