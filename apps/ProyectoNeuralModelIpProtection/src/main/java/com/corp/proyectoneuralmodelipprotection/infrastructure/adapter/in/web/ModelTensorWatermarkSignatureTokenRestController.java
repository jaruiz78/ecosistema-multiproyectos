package com.corp.proyectoneuralmodelipprotection.infrastructure.adapter.in.web;

import com.corp.proyectoneuralmodelipprotection.domain.model.ModelTensorWatermarkSignatureToken;
import com.corp.proyectoneuralmodelipprotection.domain.port.in.ManageModelTensorWatermarkSignatureTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoneuralmodelipprotection")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ModelTensorWatermarkSignatureTokenRestController {

    private final ManageModelTensorWatermarkSignatureTokenUseCase useCase;

    public ModelTensorWatermarkSignatureTokenRestController(ManageModelTensorWatermarkSignatureTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ModelTensorWatermarkSignatureToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ModelTensorWatermarkSignatureToken created = useCase.createModelTensorWatermarkSignatureToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoneuralmodelipprotection/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelTensorWatermarkSignatureToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findModelTensorWatermarkSignatureTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
