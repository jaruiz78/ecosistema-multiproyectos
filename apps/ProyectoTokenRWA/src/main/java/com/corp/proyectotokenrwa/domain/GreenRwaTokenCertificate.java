package com.corp.proyectotokenrwa.domain;

import java.util.Objects;

/**
 * Certificado inmutable de Tokenización RWA respaldado por Hidrógeno Verde y Auditoría Merkle.
 *
 * @param tokenId             Identificador único del token RWA
 * @param hydrogenBatchId     Identificador del lote de producción de hidrógeno verde
 * @param hydrogenKg          Cantidad de hidrógeno en kg respaldando el token
 * @param carbonAvoidedKgCo2  Emisiones evitadas estimadas en kg CO2
 * @param tokenValueUsd       Valor facial nominal del token en USD
 * @param merkleRootHex       Raíz del árbol de Merkle GovTech certificando el lote
 * @param commitmentHex       Compromiso de Pedersen homomórfico del valor del token
 * @param isProofVerified     Indica si la prueba Merkle de inclusión fue verificada
 * @param timestampMs         Marca temporal
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 */
public record GreenRwaTokenCertificate(
        String tokenId,
        String hydrogenBatchId,
        double hydrogenKg,
        double carbonAvoidedKgCo2,
        double tokenValueUsd,
        String merkleRootHex,
        String commitmentHex,
        boolean isProofVerified,
        long timestampMs
) {
    public GreenRwaTokenCertificate {
        Objects.requireNonNull(tokenId, "tokenId no puede ser nulo");
        Objects.requireNonNull(hydrogenBatchId, "hydrogenBatchId no puede ser nulo");
        Objects.requireNonNull(merkleRootHex, "merkleRootHex no puede ser nulo");
        Objects.requireNonNull(commitmentHex, "commitmentHex no puede ser nulo");
        if (hydrogenKg <= 0 || tokenValueUsd <= 0) {
            throw new IllegalArgumentException("Cantidades de H2 y valor nominal deben ser estrictamente positivos");
        }
    }
}
