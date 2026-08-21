package com.corp.proyectothermoelectricwasteheatharvester.infrastructure.adapter.in.web;

import com.corp.proyectothermoelectricwasteheatharvester.domain.model.SeebeckThermalGradientModuleNode;
import com.corp.proyectothermoelectricwasteheatharvester.domain.port.in.ManageSeebeckThermalGradientModuleNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectothermoelectricwasteheatharvester")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SeebeckThermalGradientModuleNodeRestController {

    private final ManageSeebeckThermalGradientModuleNodeUseCase useCase;

    public SeebeckThermalGradientModuleNodeRestController(ManageSeebeckThermalGradientModuleNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SeebeckThermalGradientModuleNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SeebeckThermalGradientModuleNode created = useCase.createSeebeckThermalGradientModuleNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectothermoelectricwasteheatharvester/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeebeckThermalGradientModuleNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSeebeckThermalGradientModuleNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
