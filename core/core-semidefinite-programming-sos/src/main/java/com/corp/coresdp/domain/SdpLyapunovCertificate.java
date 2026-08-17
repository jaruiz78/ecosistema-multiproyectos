package com.corp.coresdp.domain;

import java.io.Serializable;

/**
 * Certificado formal de estabilidad de Lyapunov y positividad polinomial SOS mediante matriz de Gram SDP.
 */
public record SdpLyapunovCertificate(
        String systemIdentifier,
        int stateDimension,
        double minEigenvalueGramMatrix,
        boolean isPositiveSemidefinite,
        double stabilityMargin
) implements Serializable {

    public static SdpLyapunovCertificate certified(String id, int dim, double minEigenvalue) {
        boolean psd = minEigenvalue >= 0.0;
        return new SdpLyapunovCertificate(id, dim, minEigenvalue, psd, Math.max(0.0, minEigenvalue));
    }
}
