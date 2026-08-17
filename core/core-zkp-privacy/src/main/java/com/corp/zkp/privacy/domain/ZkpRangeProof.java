package com.corp.zkp.privacy.domain;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Prueba de rango en conocimiento cero (Range Proof): Demuestra que un valor secreto v
 * satisface v \in [minRange, maxRange] sin revelar el valor exacto de v.
 *
 * @param commitment  Compromiso de Pedersen asociado
 * @param minRange    Límite inferior del rango
 * @param maxRange    Límite superior del rango
 * @param proofDigest Digest criptográfico SHA-256 de la prueba Fiat-Shamir
 * @param challenge   Desafío criptográfico no interactivo
 * @param response    Respuesta matemática de la prueba
 */
public record ZkpRangeProof(
        BigInteger commitment,
        long minRange,
        long maxRange,
        String proofDigest,
        BigInteger challenge,
        BigInteger response
) {
    public ZkpRangeProof {
        Objects.requireNonNull(commitment, "commitment no puede ser nulo");
        Objects.requireNonNull(proofDigest, "proofDigest no puede ser nulo");
        Objects.requireNonNull(challenge, "challenge no puede ser nulo");
        Objects.requireNonNull(response, "response no puede ser nulo");
        if (minRange > maxRange) {
            throw new IllegalArgumentException("minRange no puede ser superior a maxRange");
        }
    }
}
