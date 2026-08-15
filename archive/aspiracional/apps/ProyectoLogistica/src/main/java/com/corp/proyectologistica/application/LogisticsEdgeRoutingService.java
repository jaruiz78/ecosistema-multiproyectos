package com.corp.proyectologistica.application;

import com.corp.contracts.GeoLocationH3Record;
import com.corp.contracts.TelemetryEnkfEvent;
import com.corp.starter.edge.EdgeLiteRTBufferPool;

import java.nio.ByteBuffer;

/**
 * LogisticsEdgeRoutingService
 * Servicio de optimización logística de última milla y despacho vehicular off-heap.
 * Ejecuta cálculos de estiba, peso y ventana de entrega en O(1).
 */
public class LogisticsEdgeRoutingService {

    private final EdgeLiteRTBufferPool bufferPool;

    public LogisticsEdgeRoutingService() {
        this.bufferPool = new EdgeLiteRTBufferPool(1024, 8);
    }

    public TelemetryEnkfEvent estimateDeliveryWindow(GeoLocationH3Record origin, GeoLocationH3Record destination, double payloadKg, double maxCapacityKg) {
        ByteBuffer buffer = bufferPool.acquireBuffer();
        try {
            buffer.putDouble(payloadKg);
            buffer.putDouble(maxCapacityKg);
            buffer.flip();

            double load = buffer.getDouble();
            double cap = buffer.getDouble();
            double loadRatio = cap > 0 ? (load / cap) : 1.0;
            double etaMinutes = 15.0 + (loadRatio * 20.0);
            boolean onSchedule = loadRatio <= 1.0;
            double covariance = Math.min(0.45, loadRatio * 0.35);

            return new TelemetryEnkfEvent(
                    "vehicle-delivery-" + destination.h3Index(),
                    destination.h3Index(),
                    new double[]{load, cap, loadRatio, etaMinutes},
                    covariance,
                    System.currentTimeMillis(),
                    onSchedule
            );
        } finally {
            bufferPool.releaseBuffer(buffer);
        }
    }
}
