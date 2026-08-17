package com.corp.corealertaggregator.application;

import com.corp.corealertaggregator.domain.CorealertaggregatorEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica pura.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CorealertaggregatorUseCase {
    private final ReentrantLock lock = new ReentrantLock();

    public CorealertaggregatorEntity processLogic(CorealertaggregatorEntity input) {
        lock.lock();
        try {
            return new CorealertaggregatorEntity(
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
