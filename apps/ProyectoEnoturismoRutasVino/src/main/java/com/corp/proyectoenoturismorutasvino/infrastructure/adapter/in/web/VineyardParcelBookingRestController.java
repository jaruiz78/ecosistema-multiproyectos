package com.corp.proyectoenoturismorutasvino.infrastructure.adapter.in.web;

import com.corp.proyectoenoturismorutasvino.domain.model.VineyardParcelBooking;
import com.corp.proyectoenoturismorutasvino.domain.port.in.ManageVineyardParcelBookingUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoenoturismorutasvino")
public class VineyardParcelBookingRestController {

    private final ManageVineyardParcelBookingUseCase useCase;

    public VineyardParcelBookingRestController(ManageVineyardParcelBookingUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<VineyardParcelBooking> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        VineyardParcelBooking created = useCase.createVineyardParcelBooking(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoenoturismorutasvino/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VineyardParcelBooking> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findVineyardParcelBookingById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
