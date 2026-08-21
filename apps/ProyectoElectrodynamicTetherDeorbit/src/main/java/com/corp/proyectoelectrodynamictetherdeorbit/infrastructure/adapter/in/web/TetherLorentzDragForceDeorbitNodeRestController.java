package com.corp.proyectoelectrodynamictetherdeorbit.infrastructure.adapter.in.web;

import com.corp.proyectoelectrodynamictetherdeorbit.domain.model.TetherLorentzDragForceDeorbitNode;
import com.corp.proyectoelectrodynamictetherdeorbit.domain.port.in.ManageTetherLorentzDragForceDeorbitNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoelectrodynamictetherdeorbit")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class TetherLorentzDragForceDeorbitNodeRestController {

    private final ManageTetherLorentzDragForceDeorbitNodeUseCase useCase;

    public TetherLorentzDragForceDeorbitNodeRestController(ManageTetherLorentzDragForceDeorbitNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TetherLorentzDragForceDeorbitNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TetherLorentzDragForceDeorbitNode created = useCase.createTetherLorentzDragForceDeorbitNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoelectrodynamictetherdeorbit/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TetherLorentzDragForceDeorbitNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTetherLorentzDragForceDeorbitNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
