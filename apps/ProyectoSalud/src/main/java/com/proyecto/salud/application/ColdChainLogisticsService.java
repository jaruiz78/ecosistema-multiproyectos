package com.proyecto.salud.application;

import com.proyecto.salud.domain.BioMedicalPayload;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de monitorización de la cadena de frío en transporte urgente de muestras biológicas.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
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
