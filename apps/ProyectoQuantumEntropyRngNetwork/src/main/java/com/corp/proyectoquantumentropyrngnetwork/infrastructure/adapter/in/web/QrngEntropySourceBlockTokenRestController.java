package com.corp.proyectoquantumentropyrngnetwork.infrastructure.adapter.in.web;

import com.corp.proyectoquantumentropyrngnetwork.domain.model.QrngEntropySourceBlockToken;
import com.corp.proyectoquantumentropyrngnetwork.domain.port.in.ManageQrngEntropySourceBlockTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumentropyrngnetwork")
public class QrngEntropySourceBlockTokenRestController {

    private final ManageQrngEntropySourceBlockTokenUseCase useCase;

    public QrngEntropySourceBlockTokenRestController(ManageQrngEntropySourceBlockTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QrngEntropySourceBlockToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QrngEntropySourceBlockToken created = useCase.createQrngEntropySourceBlockToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumentropyrngnetwork/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QrngEntropySourceBlockToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQrngEntropySourceBlockTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
