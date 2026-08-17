package com.corp.proyectocriticalsupplyrisk.application.service;

import com.corp.proyectocriticalsupplyrisk.domain.model.SupplyChainNode;
import com.corp.proyectocriticalsupplyrisk.domain.port.out.SupplyChainGraphRepositoryPort;

public class SupplyChainCascadeRiskService {

    private final SupplyChainGraphRepositoryPort repositoryPort;

    public SupplyChainCascadeRiskService(SupplyChainGraphRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public SupplyChainNode evaluateChokepointVulnerability(String nodeId, double disruptionSeverity) {
        SupplyChainNode node = repositoryPort.findById(nodeId)
                .orElseGet(() -> new SupplyChainNode(nodeId, "LITHIUM_HYDROXIDE", 500.0, 2000.0, 0.4, true));

        SupplyChainNode disrupted = node.simulateDisruption(disruptionSeverity);
        return repositoryPort.save(disrupted);
    }
}
