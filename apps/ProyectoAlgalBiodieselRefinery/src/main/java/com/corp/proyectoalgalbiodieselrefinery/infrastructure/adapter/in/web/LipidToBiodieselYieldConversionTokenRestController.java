package com.corp.proyectoalgalbiodieselrefinery.infrastructure.adapter.in.web;

import com.corp.proyectoalgalbiodieselrefinery.domain.model.LipidToBiodieselYieldConversionToken;
import com.corp.proyectoalgalbiodieselrefinery.domain.port.in.ManageLipidToBiodieselYieldConversionTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoalgalbiodieselrefinery")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class LipidToBiodieselYieldConversionTokenRestController {

    private final ManageLipidToBiodieselYieldConversionTokenUseCase useCase;

    public LipidToBiodieselYieldConversionTokenRestController(ManageLipidToBiodieselYieldConversionTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LipidToBiodieselYieldConversionToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LipidToBiodieselYieldConversionToken created = useCase.createLipidToBiodieselYieldConversionToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoalgalbiodieselrefinery/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LipidToBiodieselYieldConversionToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLipidToBiodieselYieldConversionTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
