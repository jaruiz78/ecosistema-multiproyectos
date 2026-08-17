package com.corp.proyectopharmacoldchain.infrastructure.adapter.in.web;

import com.corp.proyectopharmacoldchain.domain.model.PharmaColdChain;
import com.corp.proyectopharmacoldchain.domain.port.in.ManagePharmaColdChainUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectopharmacoldchain")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class PharmaColdChainRestController {

    private final ManagePharmaColdChainUseCase useCase;

    public PharmaColdChainRestController(ManagePharmaColdChainUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PharmaColdChain> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PharmaColdChain created = useCase.createPharmaColdChain(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectopharmacoldchain/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmaColdChain> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPharmaColdChainById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
