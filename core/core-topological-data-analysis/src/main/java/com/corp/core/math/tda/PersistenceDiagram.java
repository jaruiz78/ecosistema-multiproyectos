package com.corp.core.math.tda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representación de un intervalo de persistencia \([b, d)\) en homología persistente (\(H_k\)).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record PersistenceDiagram(
        int dimension, // 0 = componentes conexas (\beta_0), 1 = lazos (\beta_1), 2 = cavidades (\beta_2)
        double birthRadius,
        double deathRadius
) implements Serializable {

    public double persistenceLifetime() {
        return Math.max(0.0, deathRadius - birthRadius);
    }
}
