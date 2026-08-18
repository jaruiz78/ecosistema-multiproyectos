package com.corp.proyectoaptamerdiagnosticbiosensors.infrastructure.adapter.in.web;

import com.corp.proyectoaptamerdiagnosticbiosensors.domain.model.AptamerDissociationConstantKdToken;
import com.corp.proyectoaptamerdiagnosticbiosensors.domain.port.in.ManageAptamerDissociationConstantKdTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoaptamerdiagnosticbiosensors")
public class AptamerDissociationConstantKdTokenRestController {

    private final ManageAptamerDissociationConstantKdTokenUseCase useCase;

    public AptamerDissociationConstantKdTokenRestController(ManageAptamerDissociationConstantKdTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AptamerDissociationConstantKdToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AptamerDissociationConstantKdToken created = useCase.createAptamerDissociationConstantKdToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoaptamerdiagnosticbiosensors/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AptamerDissociationConstantKdToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAptamerDissociationConstantKdTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
