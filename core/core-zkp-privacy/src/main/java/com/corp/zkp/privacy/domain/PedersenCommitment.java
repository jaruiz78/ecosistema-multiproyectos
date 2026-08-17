package com.corp.zkp.privacy.domain;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Compromiso criptográfico de Pedersen: C = g^v * h^r mod p.
 * Permite ocultar un valor v con un factor de aleatoriedad (blinding factor) r,
 * manteniendo propiedades de homomorfismo aditivo.
 *
 * @param commitment    Valor escalar del compromiso
 * @param commitmentHex Representación hexadecimal del compromiso
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 */
public record PedersenCommitment(
        BigInteger commitment,
        String commitmentHex
) {
    public PedersenCommitment {
        Objects.requireNonNull(commitment, "commitment no puede ser nulo");
        Objects.requireNonNull(commitmentHex, "commitmentHex no puede ser nulo");
    }
}
