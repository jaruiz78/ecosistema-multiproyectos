package com.corp.proyectoagroenergyvpp.infrastructure.adapter.in.web;

import com.corp.proyectoagroenergyvpp.domain.model.AgroEnergyVPP;
import com.corp.proyectoagroenergyvpp.domain.port.in.ManageAgroEnergyVPPUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoagroenergyvpp")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class AgroEnergyVPPRestController {

    private final ManageAgroEnergyVPPUseCase useCase;

    public AgroEnergyVPPRestController(ManageAgroEnergyVPPUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AgroEnergyVPP> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AgroEnergyVPP created = useCase.createAgroEnergyVPP(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoagroenergyvpp/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgroEnergyVPP> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAgroEnergyVPPById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
