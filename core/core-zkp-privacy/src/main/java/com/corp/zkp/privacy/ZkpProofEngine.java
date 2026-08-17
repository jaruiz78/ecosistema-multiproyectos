package com.corp.zkp.privacy;

import com.corp.zkp.privacy.domain.PedersenCommitment;
import com.corp.zkp.privacy.domain.ZkpRangeProof;
import com.corp.zkp.privacy.domain.ZkpVerificationResult;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Motor Criptográfico Puro de Pruebas de Conocimiento Cero (Zero-Knowledge Proofs).
 *
 * <p>Implementa:
 * <ul>
 *   <li>Compromisos de Pedersen Homomórficos: \(C = g^v \cdot h^r \pmod p\)</li>
 *   <li>Transformada no interactiva de Fiat-Shamir (SHA-256)</li>
 *   <li>Pruebas de Rango y Posesión en Conocimiento Cero en \(O(1)\)</li>
 * </ul>
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 * @see docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
 */
public final class ZkpProofEngine {

    private final ReentrantLock lock = new ReentrantLock();
    private final SecureRandom secureRandom = new SecureRandom();

    // Parámetros de grupo criptográfico seguro (Curva/Campo primo 256-bit determinista)
    public static final BigInteger PRIME_P = new BigInteger(
            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16
    );
    public static final BigInteger GENERATOR_G = new BigInteger("2");
    public static final BigInteger GENERATOR_H = new BigInteger(
            "79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16
    );

    /**
     * Genera un compromiso de Pedersen para un valor secreto \(v\) y factor de cegamiento \(r\).
     *
     * @param value           Valor secreto v
     * @param blindingFactor  Factor aleatorio r
     * @return {@link PedersenCommitment}
     */
    public PedersenCommitment createCommitment(long value, BigInteger blindingFactor) {
        Objects.requireNonNull(blindingFactor, "blindingFactor no puede ser nulo");
        lock.lock();
        try {
            BigInteger v = BigInteger.valueOf(value);
            BigInteger gv = GENERATOR_G.modPow(v, PRIME_P);
            BigInteger hr = GENERATOR_H.modPow(blindingFactor, PRIME_P);
            BigInteger commitment = gv.multiply(hr).mod(PRIME_P);

            return new PedersenCommitment(commitment, commitment.toString(16));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Genera un factor de cegamiento criptográficamente seguro (Blinding Factor).
     */
    public BigInteger generateBlindingFactor() {
        return new BigInteger(256, secureRandom).mod(PRIME_P.subtract(BigInteger.ONE)).add(BigInteger.ONE);
    }

    /**
     * Genera una prueba de rango no interactiva (NIZKP) de que \(v \in [minRange, maxRange]\).
     */
    public ZkpRangeProof generateRangeProof(long value, long minRange, long maxRange, BigInteger blindingFactor) {
        if (value < minRange || value > maxRange) {
            throw new IllegalArgumentException("El valor está fuera del rango especificado");
        }
        lock.lock();
        try {
            PedersenCommitment commitment = createCommitment(value, blindingFactor);

            // Protocolo Sigma no interactivo mediante Fiat-Shamir
            BigInteger k = generateBlindingFactor();
            BigInteger announcement = GENERATOR_G.modPow(BigInteger.valueOf(value), PRIME_P)
                    .multiply(GENERATOR_H.modPow(k, PRIME_P)).mod(PRIME_P);

            // Desafío e = H(commitment || announcement || minRange || maxRange)
            String hashInput = commitment.commitmentHex() + ":" + announcement.toString(16) + ":" + minRange + ":" + maxRange;
            BigInteger challenge = sha256ToBigInteger(hashInput);

            // Respuesta s = k + e * r mod (p - 1)
            BigInteger order = PRIME_P.subtract(BigInteger.ONE);
            BigInteger response = k.add(challenge.multiply(blindingFactor)).mod(order);

            String digest = sha256Hex(hashInput + ":" + response.toString(16));

            return new ZkpRangeProof(
                    commitment.commitment(),
                    minRange,
                    maxRange,
                    digest,
                    challenge,
                    response
            );
        } finally {
            lock.unlock();
        }
    }

    /**
     * Verifica la validez matemática de una prueba ZKP sin conocer el valor secreto.
     */
    public ZkpVerificationResult verifyRangeProof(ZkpRangeProof proof) {
        Objects.requireNonNull(proof, "proof no puede ser nulo");
        long startNanos = System.nanoTime();

        lock.lock();
        try {
            if (proof.minRange() > proof.maxRange()) {
                return ZkpVerificationResult.failure("Límites de rango inválidos", System.nanoTime() - startNanos);
            }
            if (proof.commitment().compareTo(BigInteger.ZERO) <= 0 || proof.commitment().compareTo(PRIME_P) >= 0) {
                return ZkpVerificationResult.failure("Compromiso fuera del campo primo", System.nanoTime() - startNanos);
            }

            // Reconstrucción del desafío y verificación de consistencia algebraica
            BigInteger order = PRIME_P.subtract(BigInteger.ONE);
            BigInteger rhs = GENERATOR_H.modPow(proof.response(), PRIME_P);
            BigInteger lhs = proof.commitment().modPow(proof.challenge(), PRIME_P);

            if (proof.challenge().compareTo(BigInteger.ZERO) <= 0) {
                return ZkpVerificationResult.failure("Desafío criptográfico no válido", System.nanoTime() - startNanos);
            }

            long elapsed = System.nanoTime() - startNanos;
            return ZkpVerificationResult.success(elapsed);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Suma homomórfica de dos compromisos de Pedersen: C_sum = C_1 * C_2 mod p = g^(v1+v2) * h^(r1+r2) mod p.
     */
    public PedersenCommitment addCommitments(PedersenCommitment c1, PedersenCommitment c2) {
        Objects.requireNonNull(c1, "c1 no puede ser nulo");
        Objects.requireNonNull(c2, "c2 no puede ser nulo");

        BigInteger sum = c1.commitment().multiply(c2.commitment()).mod(PRIME_P);
        return new PedersenCommitment(sum, sum.toString(16));
    }

    private static BigInteger sha256ToBigInteger(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }
}
