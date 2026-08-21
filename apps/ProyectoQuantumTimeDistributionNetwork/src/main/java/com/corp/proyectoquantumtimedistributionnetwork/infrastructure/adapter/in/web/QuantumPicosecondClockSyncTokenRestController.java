package com.corp.proyectoquantumtimedistributionnetwork.infrastructure.adapter.in.web;

import com.corp.proyectoquantumtimedistributionnetwork.domain.model.QuantumPicosecondClockSyncToken;
import com.corp.proyectoquantumtimedistributionnetwork.domain.port.in.ManageQuantumPicosecondClockSyncTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumtimedistributionnetwork")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuantumPicosecondClockSyncTokenRestController {

    private final ManageQuantumPicosecondClockSyncTokenUseCase useCase;

    public QuantumPicosecondClockSyncTokenRestController(ManageQuantumPicosecondClockSyncTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuantumPicosecondClockSyncToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuantumPicosecondClockSyncToken created = useCase.createQuantumPicosecondClockSyncToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumtimedistributionnetwork/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuantumPicosecondClockSyncToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuantumPicosecondClockSyncTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
