package com.corp.proyectoelectronicbillofladingepcis.infrastructure.adapter.in.web;

import com.corp.proyectoelectronicbillofladingepcis.domain.model.EpcisShippingEventRecordToken;
import com.corp.proyectoelectronicbillofladingepcis.domain.port.in.ManageEpcisShippingEventRecordTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoelectronicbillofladingepcis")
public class EpcisShippingEventRecordTokenRestController {

    private final ManageEpcisShippingEventRecordTokenUseCase useCase;

    public EpcisShippingEventRecordTokenRestController(ManageEpcisShippingEventRecordTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EpcisShippingEventRecordToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EpcisShippingEventRecordToken created = useCase.createEpcisShippingEventRecordToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoelectronicbillofladingepcis/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpcisShippingEventRecordToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEpcisShippingEventRecordTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
