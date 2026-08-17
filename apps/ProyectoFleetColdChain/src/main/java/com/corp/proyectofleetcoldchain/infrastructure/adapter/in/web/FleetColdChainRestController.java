package com.corp.proyectofleetcoldchain.infrastructure.adapter.in.web;

import com.corp.proyectofleetcoldchain.domain.model.FleetColdChain;
import com.corp.proyectofleetcoldchain.domain.port.in.ManageFleetColdChainUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectofleetcoldchain")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class FleetColdChainRestController {

    private final ManageFleetColdChainUseCase useCase;

    public FleetColdChainRestController(ManageFleetColdChainUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<FleetColdChain> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        FleetColdChain created = useCase.createFleetColdChain(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectofleetcoldchain/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FleetColdChain> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findFleetColdChainById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
