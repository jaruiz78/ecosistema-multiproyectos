package com.corp.proyectofractionalrealestaterwa.infrastructure.adapter.in.web;

import com.corp.proyectofractionalrealestaterwa.domain.model.RealEstateNotarizedTitleToken;
import com.corp.proyectofractionalrealestaterwa.domain.port.in.ManageRealEstateNotarizedTitleTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectofractionalrealestaterwa")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class RealEstateNotarizedTitleTokenRestController {

    private final ManageRealEstateNotarizedTitleTokenUseCase useCase;

    public RealEstateNotarizedTitleTokenRestController(ManageRealEstateNotarizedTitleTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<RealEstateNotarizedTitleToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        RealEstateNotarizedTitleToken created = useCase.createRealEstateNotarizedTitleToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectofractionalrealestaterwa/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealEstateNotarizedTitleToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findRealEstateNotarizedTitleTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
