package com.corp.proyectoecosystemdatamarketplace.infrastructure.adapter.in.web;

import com.corp.proyectoecosystemdatamarketplace.domain.model.DataAssetContractToken;
import com.corp.proyectoecosystemdatamarketplace.domain.port.in.ManageDataAssetContractTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoecosystemdatamarketplace")
public class DataAssetContractTokenRestController {

    private final ManageDataAssetContractTokenUseCase useCase;

    public DataAssetContractTokenRestController(ManageDataAssetContractTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DataAssetContractToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DataAssetContractToken created = useCase.createDataAssetContractToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoecosystemdatamarketplace/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataAssetContractToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDataAssetContractTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
