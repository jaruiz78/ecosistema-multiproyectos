package com.corp.proyectodiscretevariableqkdmesh.infrastructure.adapter.in.web;

import com.corp.proyectodiscretevariableqkdmesh.domain.model.DvQkdDecoyStateKeyStreamToken;
import com.corp.proyectodiscretevariableqkdmesh.domain.port.in.ManageDvQkdDecoyStateKeyStreamTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodiscretevariableqkdmesh")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DvQkdDecoyStateKeyStreamTokenRestController {

    private final ManageDvQkdDecoyStateKeyStreamTokenUseCase useCase;

    public DvQkdDecoyStateKeyStreamTokenRestController(ManageDvQkdDecoyStateKeyStreamTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DvQkdDecoyStateKeyStreamToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DvQkdDecoyStateKeyStreamToken created = useCase.createDvQkdDecoyStateKeyStreamToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodiscretevariableqkdmesh/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DvQkdDecoyStateKeyStreamToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDvQkdDecoyStateKeyStreamTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
