package com.corp.proyectoplasmonicsurfacebiosensors.infrastructure.adapter.in.web;

import com.corp.proyectoplasmonicsurfacebiosensors.domain.model.PlasmonicResonanceShiftToken;
import com.corp.proyectoplasmonicsurfacebiosensors.domain.port.in.ManagePlasmonicResonanceShiftTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoplasmonicsurfacebiosensors")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlasmonicResonanceShiftTokenRestController {

    private final ManagePlasmonicResonanceShiftTokenUseCase useCase;

    public PlasmonicResonanceShiftTokenRestController(ManagePlasmonicResonanceShiftTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PlasmonicResonanceShiftToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PlasmonicResonanceShiftToken created = useCase.createPlasmonicResonanceShiftToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoplasmonicsurfacebiosensors/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlasmonicResonanceShiftToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPlasmonicResonanceShiftTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
