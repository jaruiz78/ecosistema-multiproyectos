package com.corp.corewassersteintransport.application;

import com.corp.corewassersteintransport.domain.CorewassersteintransportEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica pura.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CorewassersteintransportUseCase {
    private final ReentrantLock lock = new ReentrantLock();

    public CorewassersteintransportEntity processLogic(CorewassersteintransportEntity input) {
        lock.lock();
        try {
            return new CorewassersteintransportEntity(
                input.id(),
                "PROCESSED",
                System.currentTimeMillis(),
                input.metricValue() * 1.05
            );
        } finally {
            lock.unlock();
        }
    }
}
