package com.corp.ecotasasoberanatax.application;

import com.corp.ecotasasoberanatax.domain.EcotasaSoberanaTaxEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para EcotasaSoberanaTax.
 */
public class EcotasaSoberanaTaxUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(EcotasaSoberanaTaxEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
