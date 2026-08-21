package com.corp.proyectocoralreefecosystempreserve.infrastructure.adapter.in.web;

import com.corp.proyectocoralreefecosystempreserve.domain.model.CoralBleachingDegreeWeekNode;
import com.corp.proyectocoralreefecosystempreserve.domain.port.in.ManageCoralBleachingDegreeWeekNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocoralreefecosystempreserve")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CoralBleachingDegreeWeekNodeRestController {

    private final ManageCoralBleachingDegreeWeekNodeUseCase useCase;

    public CoralBleachingDegreeWeekNodeRestController(ManageCoralBleachingDegreeWeekNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CoralBleachingDegreeWeekNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CoralBleachingDegreeWeekNode created = useCase.createCoralBleachingDegreeWeekNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocoralreefecosystempreserve/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoralBleachingDegreeWeekNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCoralBleachingDegreeWeekNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
