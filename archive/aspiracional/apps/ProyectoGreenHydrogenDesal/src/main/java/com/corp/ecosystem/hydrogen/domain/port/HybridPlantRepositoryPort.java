package com.corp.ecosystem.hydrogen.domain.port;

import com.corp.ecosystem.hydrogen.domain.HybridDesalHydrogenCluster;
import java.util.Optional;

public interface HybridPlantRepositoryPort {
    HybridDesalHydrogenCluster save(HybridDesalHydrogenCluster plant);
    Optional<HybridDesalHydrogenCluster> findById(HybridDesalHydrogenCluster.PlantId id);
}
