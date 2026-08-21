package com.corp.proyectocriticalsupplyrisk.domain.port.out;

import com.corp.proyectocriticalsupplyrisk.domain.model.SupplyChainNode;
import java.util.List;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface SupplyChainGraphRepositoryPort {
    SupplyChainNode save(SupplyChainNode node);
    Optional<SupplyChainNode> findById(String nodeId);
    List<SupplyChainNode> findAll();
}
