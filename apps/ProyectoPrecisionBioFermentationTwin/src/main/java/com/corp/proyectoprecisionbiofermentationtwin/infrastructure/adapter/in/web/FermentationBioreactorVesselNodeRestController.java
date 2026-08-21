package com.corp.proyectoprecisionbiofermentationtwin.infrastructure.adapter.in.web;

import com.corp.proyectoprecisionbiofermentationtwin.domain.model.FermentationBioreactorVesselNode;
import com.corp.proyectoprecisionbiofermentationtwin.domain.port.in.ManageFermentationBioreactorVesselNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoprecisionbiofermentationtwin")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class FermentationBioreactorVesselNodeRestController {

    private final ManageFermentationBioreactorVesselNodeUseCase useCase;

    public FermentationBioreactorVesselNodeRestController(ManageFermentationBioreactorVesselNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<FermentationBioreactorVesselNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        FermentationBioreactorVesselNode created = useCase.createFermentationBioreactorVesselNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoprecisionbiofermentationtwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FermentationBioreactorVesselNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findFermentationBioreactorVesselNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
