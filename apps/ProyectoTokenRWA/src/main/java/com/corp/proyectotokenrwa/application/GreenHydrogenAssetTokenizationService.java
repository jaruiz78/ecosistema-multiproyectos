package com.corp.proyectotokenrwa.application;

import com.corp.govtech.ledger.GovtechMerkleLedgerEngine;
import com.corp.govtech.ledger.GovtechMerkleLedgerEngine.LedgerTransaction;
import com.corp.govtech.ledger.GovtechMerkleLedgerEngine.MerkleBlock;
import com.corp.govtech.ledger.GovtechMerkleLedgerEngine.MerkleProof;
import com.corp.proyectotokenrwa.domain.GreenRwaTokenCertificate;
import com.corp.zkp.privacy.ZkpProofEngine;
import com.corp.zkp.privacy.domain.PedersenCommitment;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Sinergia Cruzada para Tokenización RWA de Hidrógeno Verde y Créditos de Descarbonización.
 *
 * <p>Integra:
 * <ul>
 *   <li>Libro Mayor Inmutable y Árboles de Merkle ({@link GovtechMerkleLedgerEngine}).</li>
 *   <li>Compromisos Homomórficos de Pedersen Zero-Knowledge ({@link ZkpProofEngine}).</li>
 * </ul>
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 * @see docs/adr/adr-019-zkp-privacy-pedersen-fiat-shamir.md
 */
public final class GreenHydrogenAssetTokenizationService {

    private final GovtechMerkleLedgerEngine merkleLedger;
    private final ZkpProofEngine zkpEngine;
    private final ReentrantLock lock = new ReentrantLock();

    public GreenHydrogenAssetTokenizationService(
            GovtechMerkleLedgerEngine merkleLedger,
            ZkpProofEngine zkpEngine
    ) {
        this.merkleLedger = Objects.requireNonNull(merkleLedger, "merkleLedger no puede ser nulo");
        this.zkpEngine = Objects.requireNonNull(zkpEngine, "zkpEngine no puede ser nulo");
    }

    public GreenHydrogenAssetTokenizationService() {
        this(new GovtechMerkleLedgerEngine(), new ZkpProofEngine());
    }

    /**
     * Acuña y certifica un token RWA respaldado por un lote físico de hidrógeno verde y bonos de carbono.
     *
     * @param hydrogenBatchId Identificador del lote de producción
     * @param hydrogenKg      Volumen de hidrógeno verde en kg
     * @param pricePerKgUsd   Precio facial en USD/kg
     * @return {@link GreenRwaTokenCertificate}
     */
    public GreenRwaTokenCertificate tokenizeGreenHydrogenBatch(
            String hydrogenBatchId,
            double hydrogenKg,
            double pricePerKgUsd
    ) {
        Objects.requireNonNull(hydrogenBatchId, "hydrogenBatchId no puede ser nulo");
        if (hydrogenKg <= 0 || pricePerKgUsd <= 0) {
            throw new IllegalArgumentException("Parámetros deben ser positivos");
        }

        lock.lock();
        try {
            double tokenValueUsd = hydrogenKg * pricePerKgUsd;
            double carbonAvoidedKgCo2 = hydrogenKg * 10.0;

            String txId = "TX-RWA-" + UUID.randomUUID().toString().substring(0, 8);
            String payload = String.format(
                    "{\"batch\":\"%s\",\"kg\":%.2f,\"usd\":%.2f,\"co2Avoided\":%.2f}",
                    hydrogenBatchId, hydrogenKg, tokenValueUsd, carbonAvoidedKgCo2
            );

            // 1. Registrar en el libro mayor Merkle de GovTech
            LedgerTransaction tx = new LedgerTransaction(txId, "tenant-token-rwa", payload, System.currentTimeMillis());
            MerkleBlock block = merkleLedger.appendAndSealBlock(List.of(tx));

            // Generar y verificar la prueba Merkle de inclusión
            Optional<MerkleProof> proofOpt = merkleLedger.generateProof(block.blockHeight(), txId);
            boolean proofValid = proofOpt.isPresent() && GovtechMerkleLedgerEngine.verifyProof(proofOpt.get());

            // 2. Generar compromiso homomórfico de Pedersen para el valor financiero
            BigInteger r = zkpEngine.generateBlindingFactor();
            long valueCents = Math.round(tokenValueUsd * 100);
            PedersenCommitment commitment = zkpEngine.createCommitment(valueCents, r);

            String tokenId = "TOKEN-H2RWA-" + UUID.randomUUID().toString().substring(0, 8);

            return new GreenRwaTokenCertificate(
                    tokenId,
                    hydrogenBatchId,
                    hydrogenKg,
                    carbonAvoidedKgCo2,
                    tokenValueUsd,
                    block.merkleRoot(),
                    commitment.commitmentHex(),
                    proofValid,
                    System.currentTimeMillis()
            );
        } finally {
            lock.unlock();
        }
    }
}
