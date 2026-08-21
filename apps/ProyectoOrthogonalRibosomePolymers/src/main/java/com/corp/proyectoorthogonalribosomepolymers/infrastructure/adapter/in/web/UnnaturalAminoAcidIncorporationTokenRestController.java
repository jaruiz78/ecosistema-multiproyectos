package com.corp.proyectoorthogonalribosomepolymers.infrastructure.adapter.in.web;

import com.corp.proyectoorthogonalribosomepolymers.domain.model.UnnaturalAminoAcidIncorporationToken;
import com.corp.proyectoorthogonalribosomepolymers.domain.port.in.ManageUnnaturalAminoAcidIncorporationTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoorthogonalribosomepolymers")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class UnnaturalAminoAcidIncorporationTokenRestController {

    private final ManageUnnaturalAminoAcidIncorporationTokenUseCase useCase;

    public UnnaturalAminoAcidIncorporationTokenRestController(ManageUnnaturalAminoAcidIncorporationTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<UnnaturalAminoAcidIncorporationToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        UnnaturalAminoAcidIncorporationToken created = useCase.createUnnaturalAminoAcidIncorporationToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoorthogonalribosomepolymers/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnnaturalAminoAcidIncorporationToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findUnnaturalAminoAcidIncorporationTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
