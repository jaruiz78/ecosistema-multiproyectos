package com.corp.ecosystem.quantumrwa.application;

import com.corp.ecosystem.quantumrwa.domain.QuantumSecuredInfrastructureAsset;
import com.corp.ecosystem.quantumrwa.domain.port.QuantumAssetRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuantumResistantRwaService {

    private final QuantumAssetRepositoryPort repositoryPort;

    public QuantumResistantRwaService(QuantumAssetRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public QuantumSecuredInfrastructureAsset tokenizePublicAsset(
            String tenantId,
            String assetName,
            BigDecimal totalValuationEur,
            long fractionsIssued,
            double expectedAnnualYield
    ) {
        QuantumSecuredInfrastructureAsset.AssetTokenId id = new QuantumSecuredInfrastructureAsset.AssetTokenId("RWA-PQ-" + System.nanoTime());
        QuantumSecuredInfrastructureAsset asset = QuantumSecuredInfrastructureAsset.tokenizeAsset(
                id, tenantId, assetName, totalValuationEur, fractionsIssued, expectedAnnualYield
        );
        return repositoryPort.save(asset);
    }

    public Optional<QuantumSecuredInfrastructureAsset> getAsset(QuantumSecuredInfrastructureAsset.AssetTokenId id) {
        return repositoryPort.findById(id);
    }
}
