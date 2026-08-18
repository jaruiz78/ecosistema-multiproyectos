package com.corp.proyectoasteroidresourceprospector.infrastructure.adapter.in.web;

import com.corp.proyectoasteroidresourceprospector.domain.model.AsteroidMineralAbundanceScoreToken;
import com.corp.proyectoasteroidresourceprospector.domain.port.in.ManageAsteroidMineralAbundanceScoreTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoasteroidresourceprospector")
public class AsteroidMineralAbundanceScoreTokenRestController {

    private final ManageAsteroidMineralAbundanceScoreTokenUseCase useCase;

    public AsteroidMineralAbundanceScoreTokenRestController(ManageAsteroidMineralAbundanceScoreTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AsteroidMineralAbundanceScoreToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AsteroidMineralAbundanceScoreToken created = useCase.createAsteroidMineralAbundanceScoreToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoasteroidresourceprospector/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsteroidMineralAbundanceScoreToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAsteroidMineralAbundanceScoreTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
