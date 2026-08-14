package com.proyecto.agua.application;

import com.corp.contracts.CellularIrrigationEvent;
import com.corp.starter.edge.EdgeLiteRTBufferPool;

import java.nio.ByteBuffer;

/**
 * WaterHammerInferenceService
 * Servicio de predicción analítica de transitorios hidráulicos y golpe de ariete off-heap.
 */
public class WaterHammerInferenceService {

    private final EdgeLiteRTBufferPool bufferPool;

    public WaterHammerInferenceService() {
        this.bufferPool = new EdgeLiteRTBufferPool(1024, 8);
    }

    /**
     * Ecuación de Joukowsky directa: ΔP = ρ * a * Δv
     * Ejecutada con memoria off-heap en O(1).
     */
    public CellularIrrigationEvent evaluatePressureTransient(String tenantId, String plotId, String h3Cell, double flowRateM3s, double pipeDiameterM, double valveCloseTimeSec) {
        ByteBuffer buffer = bufferPool.acquireBuffer();
        try {
            buffer.putDouble(flowRateM3s);
            buffer.putDouble(pipeDiameterM);
            buffer.putDouble(valveCloseTimeSec);
            buffer.flip();

            double q = buffer.getDouble();
            double d = buffer.getDouble();
            double tc = buffer.getDouble();

            double area = Math.PI * Math.pow(d / 2.0, 2);
            double v0 = area > 0 ? (q / area) : 0.0;
            double waveSpeed = 1000.0; // m/s en tuberías de fundición/polietileno
            double deltaPressurePa = 1000.0 * waveSpeed * (v0 / Math.max(1.0, tc));
            double deltaPressureBar = deltaPressurePa / 100_000.0;

            String status = deltaPressureBar > 16.0 ? "THROTTLED" : "OPEN";

            return new CellularIrrigationEvent(
                    tenantId,
                    plotId,
                    h3Cell,
                    q * 3600.0,
                    deltaPressureBar,
                    status,
                    System.currentTimeMillis()
            );
        } finally {
            bufferPool.releaseBuffer(buffer);
        }
    }
}
