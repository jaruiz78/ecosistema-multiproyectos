package com.corp.coretda.application;

import com.corp.core.math.tda.PersistenceDiagram;
import com.corp.core.math.tda.VietorisRipsComplex;
import com.corp.coretda.domain.TopologicalSignature;

import java.util.List;

public class TdaAnomalyDetectionUseCase {

    public TopologicalSignature analyzeStructuralMesh(String meshId, double[][] sensorReadings, double epsilonThreshold) {
        List<PersistenceDiagram> diagrams = VietorisRipsComplex.computePersistence(sensorReadings, epsilonThreshold);

        double maxLifetime = 0.0;
        int betti0 = 0;
        int betti1 = 0;

        for (var d : diagrams) {
            if (d.dimension() == 0) betti0++;
            if (d.dimension() == 1) betti1++;
            if (d.persistenceLifetime() > maxLifetime) {
                maxLifetime = d.persistenceLifetime();
            }
        }

        boolean anomaly = betti1 > 0 && maxLifetime > (epsilonThreshold * 0.5);

        return new TopologicalSignature(meshId, betti0, betti1, maxLifetime, anomaly);
    }
}
