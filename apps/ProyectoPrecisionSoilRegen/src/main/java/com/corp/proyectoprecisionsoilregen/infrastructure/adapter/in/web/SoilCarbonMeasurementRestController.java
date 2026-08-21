package com.corp.proyectoprecisionsoilregen.infrastructure.adapter.in.web;

import com.corp.proyectoprecisionsoilregen.domain.model.SoilCarbonMeasurement;
import com.corp.proyectoprecisionsoilregen.domain.port.in.ManageSoilCarbonMeasurementUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoprecisionsoilregen")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SoilCarbonMeasurementRestController {

    private final ManageSoilCarbonMeasurementUseCase useCase;

    public SoilCarbonMeasurementRestController(ManageSoilCarbonMeasurementUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SoilCarbonMeasurement> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SoilCarbonMeasurement created = useCase.createSoilCarbonMeasurement(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoprecisionsoilregen/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoilCarbonMeasurement> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSoilCarbonMeasurementById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
