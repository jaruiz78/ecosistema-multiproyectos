package com.corp.proyectosmartagrisupplychain.infrastructure.adapter.in.web;

import com.corp.proyectosmartagrisupplychain.domain.model.SmartAgriSupplyChain;
import com.corp.proyectosmartagrisupplychain.domain.port.in.ManageSmartAgriSupplyChainUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosmartagrisupplychain")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class SmartAgriSupplyChainRestController {

    private final ManageSmartAgriSupplyChainUseCase useCase;

    public SmartAgriSupplyChainRestController(ManageSmartAgriSupplyChainUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SmartAgriSupplyChain> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SmartAgriSupplyChain created = useCase.createSmartAgriSupplyChain(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosmartagrisupplychain/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmartAgriSupplyChain> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSmartAgriSupplyChainById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
