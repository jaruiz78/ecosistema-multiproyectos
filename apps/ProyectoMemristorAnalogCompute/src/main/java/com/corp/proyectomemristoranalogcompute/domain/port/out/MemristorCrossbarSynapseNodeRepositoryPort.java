package com.corp.proyectomemristoranalogcompute.domain.port.out;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface MemristorCrossbarSynapseNodeRepositoryPort {
    MemristorCrossbarSynapseNode save(MemristorCrossbarSynapseNode entity);
    Optional<MemristorCrossbarSynapseNode> findById(String id, String tenantId);
}
