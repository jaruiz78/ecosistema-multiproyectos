package com.corp.proyectolunaroxygenisruplant.infrastructure.adapter.in.web;

import com.corp.proyectolunaroxygenisruplant.domain.model.RegolithOxygenExtractionRateYieldToken;
import com.corp.proyectolunaroxygenisruplant.domain.port.in.ManageRegolithOxygenExtractionRateYieldTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectolunaroxygenisruplant")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class RegolithOxygenExtractionRateYieldTokenRestController {

    private final ManageRegolithOxygenExtractionRateYieldTokenUseCase useCase;

    public RegolithOxygenExtractionRateYieldTokenRestController(ManageRegolithOxygenExtractionRateYieldTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<RegolithOxygenExtractionRateYieldToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        RegolithOxygenExtractionRateYieldToken created = useCase.createRegolithOxygenExtractionRateYieldToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectolunaroxygenisruplant/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegolithOxygenExtractionRateYieldToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findRegolithOxygenExtractionRateYieldTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
