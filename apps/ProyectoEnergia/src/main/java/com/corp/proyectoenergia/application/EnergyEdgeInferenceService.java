package com.corp.proyectoenergia.application;

import com.corp.contracts.GeoLocationH3Record;
import com.corp.contracts.TelemetryEnkfEvent;
import com.corp.starter.edge.EdgeLiteRTBufferPool;

import java.nio.ByteBuffer;

/**
 * EnergyEdgeInferenceService
 * Servicio de inferencia edge AI y balance de red energética off-heap.
 * Garantiza complejidad O(1) y coste 0.00 USD/mes de tokens.
 */
public class EnergyEdgeInferenceService {

    private final EdgeLiteRTBufferPool bufferPool;

    public EnergyEdgeInferenceService() {
        this.bufferPool = new EdgeLiteRTBufferPool(1024, 8);
    }

    /**
     * Calcula la sobrecarga de la celda y balance reactivo usando búferes off-heap de memoria directa.
     */
    public TelemetryEnkfEvent estimateGridStability(GeoLocationH3Record location, double generationMw, double demandMw) {
        ByteBuffer buffer = bufferPool.acquireBuffer();
        try {
            buffer.putDouble(generationMw);
            buffer.putDouble(demandMw);
            buffer.flip();

            double gen = buffer.getDouble();
            double dem = buffer.getDouble();
            double reserve = gen - dem;
            double stressIndex = dem > 0 ? (dem / (gen + 0.001)) : 0.0;
            boolean stable = stressIndex <= 1.0;
            double covariance = Math.min(0.5, stressIndex * 0.4);

            return new TelemetryEnkfEvent(
                    "grid-node-" + location.h3Index(),
                    location.h3Index(),
                    new double[]{gen, dem, reserve, stressIndex},
                    covariance,
                    System.currentTimeMillis(),
                    stable
            );
        } finally {
            bufferPool.releaseBuffer(buffer);
        }
    }
}
