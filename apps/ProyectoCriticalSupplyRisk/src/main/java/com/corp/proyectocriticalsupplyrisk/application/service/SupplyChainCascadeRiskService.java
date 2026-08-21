package com.corp.proyectocriticalsupplyrisk.application.service;

import com.corp.proyectocriticalsupplyrisk.domain.model.SupplyChainNode;
import com.corp.proyectocriticalsupplyrisk.domain.port.out.SupplyChainGraphRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
