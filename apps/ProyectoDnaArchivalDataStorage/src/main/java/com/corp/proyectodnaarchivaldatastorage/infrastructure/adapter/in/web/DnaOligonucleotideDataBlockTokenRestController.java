package com.corp.proyectodnaarchivaldatastorage.infrastructure.adapter.in.web;

import com.corp.proyectodnaarchivaldatastorage.domain.model.DnaOligonucleotideDataBlockToken;
import com.corp.proyectodnaarchivaldatastorage.domain.port.in.ManageDnaOligonucleotideDataBlockTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodnaarchivaldatastorage")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DnaOligonucleotideDataBlockTokenRestController {

    private final ManageDnaOligonucleotideDataBlockTokenUseCase useCase;

    public DnaOligonucleotideDataBlockTokenRestController(ManageDnaOligonucleotideDataBlockTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DnaOligonucleotideDataBlockToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DnaOligonucleotideDataBlockToken created = useCase.createDnaOligonucleotideDataBlockToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodnaarchivaldatastorage/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DnaOligonucleotideDataBlockToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDnaOligonucleotideDataBlockTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
