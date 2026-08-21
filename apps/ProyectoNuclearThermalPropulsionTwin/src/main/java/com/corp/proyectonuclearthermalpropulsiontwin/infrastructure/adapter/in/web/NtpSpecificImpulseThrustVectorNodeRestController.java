package com.corp.proyectonuclearthermalpropulsiontwin.infrastructure.adapter.in.web;

import com.corp.proyectonuclearthermalpropulsiontwin.domain.model.NtpSpecificImpulseThrustVectorNode;
import com.corp.proyectonuclearthermalpropulsiontwin.domain.port.in.ManageNtpSpecificImpulseThrustVectorNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectonuclearthermalpropulsiontwin")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class NtpSpecificImpulseThrustVectorNodeRestController {

    private final ManageNtpSpecificImpulseThrustVectorNodeUseCase useCase;

    public NtpSpecificImpulseThrustVectorNodeRestController(ManageNtpSpecificImpulseThrustVectorNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<NtpSpecificImpulseThrustVectorNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        NtpSpecificImpulseThrustVectorNode created = useCase.createNtpSpecificImpulseThrustVectorNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectonuclearthermalpropulsiontwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NtpSpecificImpulseThrustVectorNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findNtpSpecificImpulseThrustVectorNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
