package com.corp.proyectosalud.infrastructure.adapter.in.web;

import com.corp.proyectosalud.domain.model.ClinicalTrialSubject;
import com.corp.proyectosalud.domain.port.in.ManageClinicalTrialSubjectUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosalud")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ClinicalTrialSubjectRestController {

    private final ManageClinicalTrialSubjectUseCase useCase;

    public ClinicalTrialSubjectRestController(ManageClinicalTrialSubjectUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ClinicalTrialSubject> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ClinicalTrialSubject created = useCase.createClinicalTrialSubject(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosalud/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalTrialSubject> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findClinicalTrialSubjectById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
