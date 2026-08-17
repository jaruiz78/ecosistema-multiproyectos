package com.corp.proyectob2g.infrastructure.adapter.in.web;

import com.corp.proyectob2g.domain.model.B2G;
import com.corp.proyectob2g.domain.port.in.ManageB2GUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectob2g")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class B2GRestController {

    private final ManageB2GUseCase useCase;

    public B2GRestController(ManageB2GUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<B2G> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        B2G created = useCase.createB2G(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectob2g/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<B2G> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findB2GById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
