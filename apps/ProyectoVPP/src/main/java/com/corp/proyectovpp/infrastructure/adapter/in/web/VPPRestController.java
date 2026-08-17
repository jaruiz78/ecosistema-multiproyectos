package com.corp.proyectovpp.infrastructure.adapter.in.web;

import com.corp.proyectovpp.domain.model.VPP;
import com.corp.proyectovpp.domain.port.in.ManageVPPUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectovpp")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class VPPRestController {

    private final ManageVPPUseCase useCase;

    public VPPRestController(ManageVPPUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<VPP> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        VPP created = useCase.createVPP(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectovpp/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VPP> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findVPPById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
