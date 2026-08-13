package com.proyecto.agua.application;

import com.proyecto.agua.domain.WaterNetworkNode;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de aplicación para predicción de presión hidráulica y detección de transitorios de golpe de ariete.
 * Utiliza concurrencia anti-pinning con ReentrantLock para Java 25 Virtual Threads.
 */
public class WaterPressurePinnService {

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Evalúa el residuo de presión y detecta posibles fugas o golpe de ariete (Water Hammer).
     */
    public WaterNetworkNode evaluatePressureTransient(WaterNetworkNode node, double waveCelerityMs) {
        lock.lock();
        try {
            // Cálculo determinista de sobrerregulación Joukowsky: deltaP = rho * a * deltaV
            double joukowskyPressureRiseBar = (1000.0 * waveCelerityMs * 0.5) / 100000.0; // Pa a Bar
            double predictedPressure = node.pressureBar() + joukowskyPressureRiseBar * 0.1;
            boolean anomaly = predictedPressure > 6.0 || predictedPressure < 1.0;

            return node.withPressureUpdate(Math.round(predictedPressure * 100.0) / 100.0, anomaly);
        } finally {
            lock.unlock();
        }
    }
}
