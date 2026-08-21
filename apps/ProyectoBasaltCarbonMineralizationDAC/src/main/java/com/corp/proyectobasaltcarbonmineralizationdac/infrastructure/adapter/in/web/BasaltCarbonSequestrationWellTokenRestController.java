package com.corp.proyectobasaltcarbonmineralizationdac.infrastructure.adapter.in.web;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.in.ManageBasaltCarbonSequestrationWellTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobasaltcarbonmineralizationdac")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BasaltCarbonSequestrationWellTokenRestController {

    private final ManageBasaltCarbonSequestrationWellTokenUseCase useCase;

    public BasaltCarbonSequestrationWellTokenRestController(ManageBasaltCarbonSequestrationWellTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BasaltCarbonSequestrationWellToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BasaltCarbonSequestrationWellToken created = useCase.createBasaltCarbonSequestrationWellToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobasaltcarbonmineralizationdac/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BasaltCarbonSequestrationWellToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBasaltCarbonSequestrationWellTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
