package com.corp.proyectoomnisystemicplanetarytwin.infrastructure.adapter.in.web;

import com.corp.proyectoomnisystemicplanetarytwin.domain.model.PlanetaryTensorsNexusNode;
import com.corp.proyectoomnisystemicplanetarytwin.domain.port.in.ManagePlanetaryTensorsNexusNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoomnisystemicplanetarytwin")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlanetaryTensorsNexusNodeRestController {

    private final ManagePlanetaryTensorsNexusNodeUseCase useCase;

    public PlanetaryTensorsNexusNodeRestController(ManagePlanetaryTensorsNexusNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PlanetaryTensorsNexusNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PlanetaryTensorsNexusNode created = useCase.createPlanetaryTensorsNexusNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoomnisystemicplanetarytwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanetaryTensorsNexusNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPlanetaryTensorsNexusNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
