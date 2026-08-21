package com.corp.proyectoneuralbciaccessibilitycontrol.infrastructure.adapter.in.web;

import com.corp.proyectoneuralbciaccessibilitycontrol.domain.model.BciNeuralMotorIntentEventNode;
import com.corp.proyectoneuralbciaccessibilitycontrol.domain.port.in.ManageBciNeuralMotorIntentEventNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoneuralbciaccessibilitycontrol")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BciNeuralMotorIntentEventNodeRestController {

    private final ManageBciNeuralMotorIntentEventNodeUseCase useCase;

    public BciNeuralMotorIntentEventNodeRestController(ManageBciNeuralMotorIntentEventNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BciNeuralMotorIntentEventNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BciNeuralMotorIntentEventNode created = useCase.createBciNeuralMotorIntentEventNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoneuralbciaccessibilitycontrol/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BciNeuralMotorIntentEventNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBciNeuralMotorIntentEventNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
