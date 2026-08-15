package com.corp.taxcomplianceledger.application;

import com.corp.taxcomplianceledger.domain.TaxComplianceLedgerEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para TaxComplianceLedger.
 */
public class TaxComplianceLedgerUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(TaxComplianceLedgerEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
