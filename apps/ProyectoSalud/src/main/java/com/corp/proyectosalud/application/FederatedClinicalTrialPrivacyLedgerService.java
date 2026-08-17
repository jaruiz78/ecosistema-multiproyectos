package com.corp.proyectosalud.application;

import com.corp.crypto.pqc.PostQuantumSecurityManager;
import com.corp.crypto.pqc.domain.PqcCipherEnvelope;
import com.corp.crypto.pqc.domain.PqcKeyPair;
import com.corp.proyectosalud.domain.ZkClinicalTrialLedgerEntry;
import com.corp.zkp.privacy.ZkpProofEngine;
import com.corp.zkp.privacy.domain.PedersenCommitment;
import com.corp.zkp.privacy.domain.ZkpRangeProof;
import com.corp.zkp.privacy.domain.ZkpVerificationResult;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Sinergia Cruzada de Privacidad Federada y Blindaje Criptográfico Post-Cuántico para Salud.
 *
 * <p>Integra:
 * <ul>
 *   <li>Compromisos homomórficos de Pedersen y pruebas de rango no interactivas ZKP ({@link ZkpProofEngine}).</li>
 *   <li>Encapsulación de secretos y cifrado post-cuántico NIST ML-KEM ({@link PostQuantumSecurityManager}).</li>
 * </ul>
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 * @see docs/adr/adr-019-zkp-privacy-pedersen-fiat-shamir.md
 */
public final class FederatedClinicalTrialPrivacyLedgerService {

    private final ZkpProofEngine zkpEngine;
    private final PostQuantumSecurityManager pqcManager;
    private final ReentrantLock lock = new ReentrantLock();

    public FederatedClinicalTrialPrivacyLedgerService(
            ZkpProofEngine zkpEngine,
            PostQuantumSecurityManager pqcManager
    ) {
        this.zkpEngine = Objects.requireNonNull(zkpEngine, "zkpEngine no puede ser nulo");
        this.pqcManager = Objects.requireNonNull(pqcManager, "pqcManager no puede ser nulo");
    }

    public FederatedClinicalTrialPrivacyLedgerService() {
        this(new ZkpProofEngine(), new PostQuantumSecurityManager());
    }

    /**
     * Procesa y certifica un lote de biomarcadores de un ensayo clínico con privacidad total Zero-PII.
     *
     * @param studyCohortId  Identificador de la cohorte del ensayo
     * @param patientMetric1 Métrica clínica nodo A (secreto 1)
     * @param patientMetric2 Métrica clínica nodo B (secreto 2)
     * @param minAllowed     Cota inferior del protocolo de ensayo
     * @param maxAllowed     Cota superior del protocolo de ensayo
     * @return {@link ZkClinicalTrialLedgerEntry}
     */
    public ZkClinicalTrialLedgerEntry processConfidentialTrialBatch(
            String studyCohortId,
            long patientMetric1,
            long patientMetric2,
            long minAllowed,
            long maxAllowed
    ) {
        Objects.requireNonNull(studyCohortId, "studyCohortId no puede ser nulo");
        long startNanos = System.nanoTime();

        lock.lock();
        try {
            // 1. Compromisos de Pedersen homomórficos para métricas individuales
            BigInteger r1 = zkpEngine.generateBlindingFactor();
            BigInteger r2 = zkpEngine.generateBlindingFactor();

            PedersenCommitment c1 = zkpEngine.createCommitment(patientMetric1, r1);
            PedersenCommitment c2 = zkpEngine.createCommitment(patientMetric2, r2);

            // Suma homomórfica C_sum = C1 * C2 mod p (Agregación federada sin revelar valores individuales)
            PedersenCommitment cSum = zkpEngine.addCommitments(c1, c2);

            // 2. Generación y Verificación de Prueba de Rango ZKP para paciente 1
            ZkpRangeProof rangeProof = zkpEngine.generateRangeProof(patientMetric1, minAllowed, maxAllowed, r1);
            ZkpVerificationResult proofResult = zkpEngine.verifyRangeProof(rangeProof);

            // 3. Encapsulación en sobre seguro post-cuántico (NIST ML-KEM)
            PqcKeyPair keyPair = pqcManager.generateKeyPair("ML-KEM-768");
            String payloadSummary = "COHORT:" + studyCohortId + ":AGG_COMMITMENT:" + cSum.commitmentHex();
            PqcCipherEnvelope envelope = pqcManager.encapsulate(keyPair, payloadSummary);

            long latencyNanos = System.nanoTime() - startNanos;
            String entryId = "ZK-TRIAL-" + UUID.randomUUID().toString().substring(0, 8);

            return new ZkClinicalTrialLedgerEntry(
                    entryId,
                    studyCohortId,
                    cSum.commitmentHex(),
                    proofResult.isValid(),
                    envelope.cipherTextHex(),
                    latencyNanos,
                    System.currentTimeMillis()
            );
        } finally {
            lock.unlock();
        }
    }
}
