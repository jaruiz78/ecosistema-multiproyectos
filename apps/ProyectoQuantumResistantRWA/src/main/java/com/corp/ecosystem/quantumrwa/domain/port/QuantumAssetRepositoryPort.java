package com.corp.ecosystem.quantumrwa.domain.port;

import com.corp.ecosystem.quantumrwa.domain.QuantumSecuredInfrastructureAsset;
import java.util.Optional;

public interface QuantumAssetRepositoryPort {
    QuantumSecuredInfrastructureAsset save(QuantumSecuredInfrastructureAsset asset);
    Optional<QuantumSecuredInfrastructureAsset> findById(QuantumSecuredInfrastructureAsset.AssetTokenId id);
}
