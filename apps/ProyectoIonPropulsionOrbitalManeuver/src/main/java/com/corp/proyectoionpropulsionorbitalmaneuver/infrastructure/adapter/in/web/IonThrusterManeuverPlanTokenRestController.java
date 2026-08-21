package com.corp.proyectoionpropulsionorbitalmaneuver.infrastructure.adapter.in.web;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.in.ManageIonThrusterManeuverPlanTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoionpropulsionorbitalmaneuver")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class IonThrusterManeuverPlanTokenRestController {

    private final ManageIonThrusterManeuverPlanTokenUseCase useCase;

    public IonThrusterManeuverPlanTokenRestController(ManageIonThrusterManeuverPlanTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<IonThrusterManeuverPlanToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        IonThrusterManeuverPlanToken created = useCase.createIonThrusterManeuverPlanToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoionpropulsionorbitalmaneuver/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IonThrusterManeuverPlanToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findIonThrusterManeuverPlanTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
