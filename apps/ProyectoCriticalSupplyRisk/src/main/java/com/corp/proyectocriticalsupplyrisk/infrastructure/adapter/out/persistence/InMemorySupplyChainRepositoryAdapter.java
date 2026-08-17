package com.corp.proyectocriticalsupplyrisk.infrastructure.adapter.out.persistence;

import com.corp.proyectocriticalsupplyrisk.domain.model.SupplyChainNode;
import com.corp.proyectocriticalsupplyrisk.domain.port.out.SupplyChainGraphRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySupplyChainRepositoryAdapter implements SupplyChainGraphRepositoryPort {

    private final Map<String, SupplyChainNode> nodes = new ConcurrentHashMap<>();

    @Override
    public SupplyChainNode save(SupplyChainNode node) {
        nodes.put(node.nodeId(), node);
        return node;
    }

    @Override
    public Optional<SupplyChainNode> findById(String nodeId) {
        return Optional.ofNullable(nodes.get(nodeId));
    }

    @Override
    public List<SupplyChainNode> findAll() {
        return new ArrayList<>(nodes.values());
    }
}
