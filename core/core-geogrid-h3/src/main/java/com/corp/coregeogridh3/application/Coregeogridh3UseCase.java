package com.corp.coregeogridh3.application;

import com.corp.coregeogridh3.domain.Coregeogridh3Entity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica pura.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class Coregeogridh3UseCase {
    private final ReentrantLock lock = new ReentrantLock();

    public Coregeogridh3Entity processLogic(Coregeogridh3Entity input) {
        lock.lock();
        try {
            return new Coregeogridh3Entity(
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
