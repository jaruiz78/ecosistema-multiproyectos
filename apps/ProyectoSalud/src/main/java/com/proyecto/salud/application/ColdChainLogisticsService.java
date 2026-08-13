package com.proyecto.salud.application;

import com.proyecto.salud.domain.BioMedicalPayload;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de monitorización de la cadena de frío en transporte urgente de muestras biológicas.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
 */
public class ColdChainLogisticsService {

    private final ReentrantLock lock = new ReentrantLock();

    public BioMedicalPayload recordTelemetry(BioMedicalPayload payload, double sensorTempCelsius) {
        lock.lock();
        try {
            return payload.withTemperature(sensorTempCelsius);
        } finally {
            lock.unlock();
        }
    }
}
