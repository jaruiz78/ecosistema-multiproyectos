package com.corp.proyectogeothermallithiumextraction.infrastructure.adapter.in.web;

import com.corp.proyectogeothermallithiumextraction.domain.model.GeothermalBrineLithiumYieldToken;
import com.corp.proyectogeothermallithiumextraction.domain.port.in.ManageGeothermalBrineLithiumYieldTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectogeothermallithiumextraction")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GeothermalBrineLithiumYieldTokenRestController {

    private final ManageGeothermalBrineLithiumYieldTokenUseCase useCase;

    public GeothermalBrineLithiumYieldTokenRestController(ManageGeothermalBrineLithiumYieldTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GeothermalBrineLithiumYieldToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GeothermalBrineLithiumYieldToken created = useCase.createGeothermalBrineLithiumYieldToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectogeothermallithiumextraction/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeothermalBrineLithiumYieldToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGeothermalBrineLithiumYieldTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
