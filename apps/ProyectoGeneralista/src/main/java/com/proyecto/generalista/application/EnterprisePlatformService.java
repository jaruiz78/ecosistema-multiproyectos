package com.proyecto.generalista.application;

import com.proyecto.generalista.domain.EnterpriseTask;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de plataforma empresarial generalista.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
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
