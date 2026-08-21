package com.corp.proyectocoastalsurgeflooddefense.infrastructure.adapter.in.web;

import com.corp.proyectocoastalsurgeflooddefense.domain.model.StormSurgeElevationForecastNode;
import com.corp.proyectocoastalsurgeflooddefense.domain.port.in.ManageStormSurgeElevationForecastNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocoastalsurgeflooddefense")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class StormSurgeElevationForecastNodeRestController {

    private final ManageStormSurgeElevationForecastNodeUseCase useCase;

    public StormSurgeElevationForecastNodeRestController(ManageStormSurgeElevationForecastNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<StormSurgeElevationForecastNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        StormSurgeElevationForecastNode created = useCase.createStormSurgeElevationForecastNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocoastalsurgeflooddefense/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StormSurgeElevationForecastNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findStormSurgeElevationForecastNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
