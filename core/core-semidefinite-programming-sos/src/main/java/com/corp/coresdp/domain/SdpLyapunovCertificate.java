package com.corp.coresdp.domain;

import java.io.Serializable;

/**
 * Certificado formal de estabilidad de Lyapunov y positividad polinomial SOS mediante matriz de Gram SDP.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
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
