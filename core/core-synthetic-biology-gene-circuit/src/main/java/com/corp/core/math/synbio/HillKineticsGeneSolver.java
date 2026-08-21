package com.corp.core.math.synbio;

import java.io.Serializable;

/**
 * Cinética de transcripción y represión genética modelada mediante la función de Hill:
 * \[
 * f(x) = \frac{\beta \cdot x^n}{K^n + x^n} \quad (\text{Activación}), \qquad g(x) = \frac{\beta}{1 + (x / K)^n} \quad (\text{Represión})
 * \]
 * donde \(\beta\) es la tasa máxima de expresión, \(K\) es la constante de disociación y \(n\) es el coeficiente de cooperatividad de Hill.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record HillKineticsGeneSolver() implements Serializable {

    public static double computeActivationRate(double transcriptionFactorConc, double maxBeta, double dissociationConstantK, double hillCoeffN) {
        if (transcriptionFactorConc <= 0.0) return 0.0;
        double xPow = Math.pow(transcriptionFactorConc, hillCoeffN);
        double kPow = Math.pow(dissociationConstantK, hillCoeffN);
        return (maxBeta * xPow) / (kPow + xPow);
    }

    public static double computeRepressionRate(double repressorConc, double maxBeta, double dissociationConstantK, double hillCoeffN) {
        if (repressorConc <= 0.0) return maxBeta;
        double ratioPow = Math.pow(repressorConc / dissociationConstantK, hillCoeffN);
        return maxBeta / (1.0 + ratioPow);
    }
}
