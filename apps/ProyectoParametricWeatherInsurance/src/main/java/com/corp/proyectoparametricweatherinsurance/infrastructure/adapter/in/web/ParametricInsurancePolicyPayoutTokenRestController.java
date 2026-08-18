package com.corp.proyectoparametricweatherinsurance.infrastructure.adapter.in.web;

import com.corp.proyectoparametricweatherinsurance.domain.model.ParametricInsurancePolicyPayoutToken;
import com.corp.proyectoparametricweatherinsurance.domain.port.in.ManageParametricInsurancePolicyPayoutTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoparametricweatherinsurance")
public class ParametricInsurancePolicyPayoutTokenRestController {

    private final ManageParametricInsurancePolicyPayoutTokenUseCase useCase;

    public ParametricInsurancePolicyPayoutTokenRestController(ManageParametricInsurancePolicyPayoutTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ParametricInsurancePolicyPayoutToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ParametricInsurancePolicyPayoutToken created = useCase.createParametricInsurancePolicyPayoutToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoparametricweatherinsurance/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametricInsurancePolicyPayoutToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findParametricInsurancePolicyPayoutTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
