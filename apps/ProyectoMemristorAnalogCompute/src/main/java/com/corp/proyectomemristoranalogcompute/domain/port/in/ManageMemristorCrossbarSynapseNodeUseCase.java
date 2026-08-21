package com.corp.proyectomemristoranalogcompute.domain.port.in;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageMemristorCrossbarSynapseNodeUseCase {
    MemristorCrossbarSynapseNode createMemristorCrossbarSynapseNode(String tenantId, String title, double value);
    Optional<MemristorCrossbarSynapseNode> findMemristorCrossbarSynapseNodeById(String id, String tenantId);
    MemristorCrossbarSynapseNode processOptimization(String id, String tenantId);
}
