package com.corp.proyectosyntheticmicrobiomeregen.infrastructure.adapter.in.web;

import com.corp.proyectosyntheticmicrobiomeregen.domain.model.SoilMicrobiomeMetabolicNode;
import com.corp.proyectosyntheticmicrobiomeregen.domain.port.in.ManageSoilMicrobiomeMetabolicNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosyntheticmicrobiomeregen")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SoilMicrobiomeMetabolicNodeRestController {

    private final ManageSoilMicrobiomeMetabolicNodeUseCase useCase;

    public SoilMicrobiomeMetabolicNodeRestController(ManageSoilMicrobiomeMetabolicNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SoilMicrobiomeMetabolicNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SoilMicrobiomeMetabolicNode created = useCase.createSoilMicrobiomeMetabolicNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosyntheticmicrobiomeregen/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoilMicrobiomeMetabolicNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSoilMicrobiomeMetabolicNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
