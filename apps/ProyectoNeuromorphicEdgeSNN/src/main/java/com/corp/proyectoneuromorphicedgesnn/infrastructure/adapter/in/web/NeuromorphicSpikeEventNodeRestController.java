package com.corp.proyectoneuromorphicedgesnn.infrastructure.adapter.in.web;

import com.corp.proyectoneuromorphicedgesnn.domain.model.NeuromorphicSpikeEventNode;
import com.corp.proyectoneuromorphicedgesnn.domain.port.in.ManageNeuromorphicSpikeEventNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoneuromorphicedgesnn")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class NeuromorphicSpikeEventNodeRestController {

    private final ManageNeuromorphicSpikeEventNodeUseCase useCase;

    public NeuromorphicSpikeEventNodeRestController(ManageNeuromorphicSpikeEventNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<NeuromorphicSpikeEventNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        NeuromorphicSpikeEventNode created = useCase.createNeuromorphicSpikeEventNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoneuromorphicedgesnn/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeuromorphicSpikeEventNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findNeuromorphicSpikeEventNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
