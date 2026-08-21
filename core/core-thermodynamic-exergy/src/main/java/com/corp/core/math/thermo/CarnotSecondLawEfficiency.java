package com.corp.core.math.thermo;

import java.io.Serializable;

/**
 * Eficiencia según la Segunda Ley de la Termodinámica (\(\eta_{II} = \frac{\dot{E}x_{\text{recuperada}}}{\dot{E}x_{\text{suministrada}}}\)).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CarnotSecondLawEfficiency() implements Serializable {

    public static double evaluateSecondLawEfficiency(double usefulExergyOutputKw, double totalExergyInputKw) {
        if (totalExergyInputKw <= 0.0) return 0.0;
        return Math.clamp(usefulExergyOutputKw / totalExergyInputKw, 0.0, 1.0);
    }
}
