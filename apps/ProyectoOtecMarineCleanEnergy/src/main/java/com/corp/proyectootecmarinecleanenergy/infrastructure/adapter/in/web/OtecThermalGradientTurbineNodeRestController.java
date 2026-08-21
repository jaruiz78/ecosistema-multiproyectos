package com.corp.proyectootecmarinecleanenergy.infrastructure.adapter.in.web;

import com.corp.proyectootecmarinecleanenergy.domain.model.OtecThermalGradientTurbineNode;
import com.corp.proyectootecmarinecleanenergy.domain.port.in.ManageOtecThermalGradientTurbineNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectootecmarinecleanenergy")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class OtecThermalGradientTurbineNodeRestController {

    private final ManageOtecThermalGradientTurbineNodeUseCase useCase;

    public OtecThermalGradientTurbineNodeRestController(ManageOtecThermalGradientTurbineNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<OtecThermalGradientTurbineNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        OtecThermalGradientTurbineNode created = useCase.createOtecThermalGradientTurbineNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectootecmarinecleanenergy/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OtecThermalGradientTurbineNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findOtecThermalGradientTurbineNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
