package com.corp.proyectogcpzerocostpubsubbatcher.infrastructure.adapter.in.web;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import com.corp.proyectogcpzerocostpubsubbatcher.domain.port.in.ManagePubSubSnappyCompressedBatchNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectogcpzerocostpubsubbatcher")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PubSubSnappyCompressedBatchNodeRestController {

    private final ManagePubSubSnappyCompressedBatchNodeUseCase useCase;

    public PubSubSnappyCompressedBatchNodeRestController(ManagePubSubSnappyCompressedBatchNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PubSubSnappyCompressedBatchNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PubSubSnappyCompressedBatchNode created = useCase.createPubSubSnappyCompressedBatchNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectogcpzerocostpubsubbatcher/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PubSubSnappyCompressedBatchNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPubSubSnappyCompressedBatchNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
