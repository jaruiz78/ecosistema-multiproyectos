package com.corp.core.math.thermo;

import java.io.Serializable;

/**
 * Eficiencia según la Segunda Ley de la Termodinámica (\(\eta_{II} = \frac{\dot{E}x_{\text{recuperada}}}{\dot{E}x_{\text{suministrada}}}\)).
 */
public record CarnotSecondLawEfficiency() implements Serializable {

    public static double evaluateSecondLawEfficiency(double usefulExergyOutputKw, double totalExergyInputKw) {
        if (totalExergyInputKw <= 0.0) return 0.0;
        return Math.clamp(usefulExergyOutputKw / totalExergyInputKw, 0.0, 1.0);
    }
}
