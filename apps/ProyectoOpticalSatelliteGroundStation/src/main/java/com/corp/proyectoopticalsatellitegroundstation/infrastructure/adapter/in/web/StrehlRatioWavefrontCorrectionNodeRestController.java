package com.corp.proyectoopticalsatellitegroundstation.infrastructure.adapter.in.web;

import com.corp.proyectoopticalsatellitegroundstation.domain.model.StrehlRatioWavefrontCorrectionNode;
import com.corp.proyectoopticalsatellitegroundstation.domain.port.in.ManageStrehlRatioWavefrontCorrectionNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoopticalsatellitegroundstation")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class StrehlRatioWavefrontCorrectionNodeRestController {

    private final ManageStrehlRatioWavefrontCorrectionNodeUseCase useCase;

    public StrehlRatioWavefrontCorrectionNodeRestController(ManageStrehlRatioWavefrontCorrectionNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<StrehlRatioWavefrontCorrectionNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        StrehlRatioWavefrontCorrectionNode created = useCase.createStrehlRatioWavefrontCorrectionNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoopticalsatellitegroundstation/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrehlRatioWavefrontCorrectionNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findStrehlRatioWavefrontCorrectionNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
