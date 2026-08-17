package com.corp.coresdp.application;

import com.corp.core.math.sdp.SumOfSquaresRelaxationSolver;
import com.corp.coresdp.domain.SdpLyapunovCertificate;

import java.io.Serializable;

/**
 * Caso de uso para certificar formalmente la estabilidad de sistemas de control óptimo (VPP, órbitas, reactores).
 */
public class SemidefiniteProgrammingUseCase implements Serializable {

    public SdpLyapunovCertificate verifyNonlinearSystemStability(String systemId, double[][] gramMatrix) {
        return SumOfSquaresRelaxationSolver.certifyStability(systemId, gramMatrix);
    }
}
