package com.proyecto.generalista.application;

import com.proyecto.generalista.domain.EnterpriseTask;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de plataforma empresarial generalista.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class EnterprisePlatformService {

    private final ReentrantLock lock = new ReentrantLock();

    public EnterpriseTask processTask(EnterpriseTask task) {
        lock.lock();
        try {
            return task.withStatus(true);
        } finally {
            lock.unlock();
        }
    }
}
