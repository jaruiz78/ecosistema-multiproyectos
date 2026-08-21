package com.corp.coresdp.application;

import com.corp.core.math.sdp.SumOfSquaresRelaxationSolver;
import com.corp.coresdp.domain.SdpLyapunovCertificate;

import java.io.Serializable;

/**
 * Caso de uso para certificar formalmente la estabilidad de sistemas de control óptimo (VPP, órbitas, reactores).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SemidefiniteProgrammingUseCase implements Serializable {

    public SdpLyapunovCertificate verifyNonlinearSystemStability(String systemId, double[][] gramMatrix) {
        return SumOfSquaresRelaxationSolver.certifyStability(systemId, gramMatrix);
    }
}
