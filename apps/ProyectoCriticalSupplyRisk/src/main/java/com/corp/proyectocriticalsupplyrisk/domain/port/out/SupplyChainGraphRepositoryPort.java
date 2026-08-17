package com.corp.proyectocriticalsupplyrisk.domain.port.out;

import com.corp.proyectocriticalsupplyrisk.domain.model.SupplyChainNode;
import java.util.List;
import java.util.Optional;

public interface SupplyChainGraphRepositoryPort {
    SupplyChainNode save(SupplyChainNode node);
    Optional<SupplyChainNode> findById(String nodeId);
    List<SupplyChainNode> findAll();
}
