package com.corp.coretda.domain;

import java.io.Serializable;

public record TopologicalSignature(
        String sensorMeshId,
        int betti0ConnectedComponents,
        int betti1Loops,
        double maxPersistenceLifetime,
        boolean anomalyDetected
) implements Serializable {}
