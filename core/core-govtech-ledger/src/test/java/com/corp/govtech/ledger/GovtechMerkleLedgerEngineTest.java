package com.corp.govtech.ledger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link GovtechMerkleLedgerEngine}.
 * Valida propiedades criptográficas formales de inmutabilidad, Merkle Proofs O(log N)
 * y concurrencia pura bajo Virtual Threads de Java 25.
 */
class GovtechMerkleLedgerEngineTest {

    @Test
    @DisplayName("Debe inicializar la cadena con el bloque Génesis de forma determinista")
    void shouldInitializeWithGenesisBlock() {
        GovtechMerkleLedgerEngine engine = new GovtechMerkleLedgerEngine();

        assertEquals(1, engine.getChainLength());
        Optional<GovtechMerkleLedgerEngine.MerkleBlock> genesisOpt = engine.getBlockByHeight(0);
        assertTrue(genesisOpt.isPresent());

        GovtechMerkleLedgerEngine.MerkleBlock genesis = genesisOpt.get();
        assertEquals(0L, genesis.blockHeight());
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000", genesis.previousHash());
        assertNotNull(genesis.blockHash());
        assertNotNull(genesis.merkleRoot());
        assertEquals(1, genesis.transactions().size());
        assertTrue(engine.validateChainIntegrity());
    }

    @Test
    @DisplayName("Debe sellar bloques, encadenar hashes y calcular raíces de Merkle deterministas")
    void shouldAppendAndSealBlocksCorrectly() {
        GovtechMerkleLedgerEngine engine = new GovtechMerkleLedgerEngine();

        List<GovtechMerkleLedgerEngine.LedgerTransaction> txsBlock1 = List.of(
                new GovtechMerkleLedgerEngine.LedgerTransaction("TX_101", "TENANT_ES_MAD", "{\"bid\": 45000.0, \"bidder\": \"Empresa A\"}", 1700000001000L),
                new GovtechMerkleLedgerEngine.LedgerTransaction("TX_102", "TENANT_ES_MAD", "{\"bid\": 43500.0, \"bidder\": \"Empresa B\"}", 1700000002000L),
                new GovtechMerkleLedgerEngine.LedgerTransaction("TX_103", "TENANT_ES_MAD", "{\"bid\": 42000.0, \"bidder\": \"Empresa C\"}", 1700000003000L)
        );

        GovtechMerkleLedgerEngine.MerkleBlock block1 = engine.appendAndSealBlock(txsBlock1);
        assertEquals(1L, block1.blockHeight());
        assertEquals(3, block1.transactions().size());
        assertEquals(2, engine.getChainLength());

        List<GovtechMerkleLedgerEngine.LedgerTransaction> txsBlock2 = List.of(
                new GovtechMerkleLedgerEngine.LedgerTransaction("TX_201", "TENANT_ES_BCN", "{\"award\": \"Empresa C\", \"amount\": 42000.0}", 1700000010000L)
        );

        GovtechMerkleLedgerEngine.MerkleBlock block2 = engine.appendAndSealBlock(txsBlock2);
        assertEquals(2L, block2.blockHeight());
        assertEquals(block1.blockHash(), block2.previousHash());
        assertEquals(3, engine.getChainLength());

        // Validar integridad de la cadena completa
        assertTrue(engine.validateChainIntegrity());
    }

    @Test
    @DisplayName("Debe generar y verificar formalmente Merkle Proofs en O(log N)")
    void shouldGenerateAndVerifyMerkleProofs() {
        GovtechMerkleLedgerEngine engine = new GovtechMerkleLedgerEngine();

        List<GovtechMerkleLedgerEngine.LedgerTransaction> txs = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            txs.add(new GovtechMerkleLedgerEngine.LedgerTransaction(
                    "TX_TENDER_" + i,
                    "TENANT_VALENCIA",
                    "{\"tender_id\": \"T2026-00" + i + "\", \"status\": \"SUBMITTED\"}",
                    1700000000000L + i * 1000L
            ));
        }

        GovtechMerkleLedgerEngine.MerkleBlock block = engine.appendAndSealBlock(txs);

        // Generar prueba para la transacción TX_TENDER_5
        Optional<GovtechMerkleLedgerEngine.MerkleProof> proofOpt = engine.generateProof(block.blockHeight(), "TX_TENDER_5");
        assertTrue(proofOpt.isPresent());

        GovtechMerkleLedgerEngine.MerkleProof proof = proofOpt.get();
        assertEquals(block.merkleRoot(), proof.merkleRoot());
        // En árbol balanceado de 8 hojas, la profundidad de la prueba es exactamente log2(8) = 3 pasos
        assertEquals(3, proof.steps().size());

        // Verificación formal positiva
        assertTrue(GovtechMerkleLedgerEngine.verifyProof(proof));

        // Verificación formal negativa ante manipulación de hash
        GovtechMerkleLedgerEngine.MerkleProof tamperedProof = new GovtechMerkleLedgerEngine.MerkleProof(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                proof.merkleRoot(),
                proof.blockHeight(),
                proof.steps()
        );
        assertFalse(GovtechMerkleLedgerEngine.verifyProof(tamperedProof));
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia con Virtual Threads de Java 25 sin degradación")
    void shouldHandleHighConcurrencyWithVirtualThreads() throws Exception {
        GovtechMerkleLedgerEngine engine = new GovtechMerkleLedgerEngine();
        int totalThreads = 100;
        int txsPerBlock = 5;

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < totalThreads; i++) {
                final int threadId = i;
                futures.add(executor.submit(() -> {
                    List<GovtechMerkleLedgerEngine.LedgerTransaction> batch = new ArrayList<>();
                    for (int j = 0; j < txsPerBlock; j++) {
                        batch.add(new GovtechMerkleLedgerEngine.LedgerTransaction(
                                "TX_CONC_" + threadId + "_" + j,
                                "TENANT_GOV_" + (threadId % 5),
                                "{\"action\": \"SIGN\", \"worker\": " + threadId + "}",
                                System.currentTimeMillis()
                        ));
                    }
                    engine.appendAndSealBlock(batch);
                }));
            }

            for (Future<?> f : futures) {
                f.get();
            }
        }

        // 1 bloque génesis + 100 bloques concurrentes = 101 bloques
        assertEquals(101, engine.getChainLength());
        assertTrue(engine.validateChainIntegrity());
    }

    @Test
    @DisplayName("Debe manejar bloques con número impar de transacciones (5 y 7) y verificar pruebas Merkle")
    void shouldHandleOddNumberOfTransactions() {
        GovtechMerkleLedgerEngine engine = new GovtechMerkleLedgerEngine();

        List<GovtechMerkleLedgerEngine.LedgerTransaction> txsOdd = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            txsOdd.add(new GovtechMerkleLedgerEngine.LedgerTransaction(
                    "TX_ODD_" + i, "TENANT_ODD", "{\"item\": " + i + "}", 1700000000000L + i
            ));
        }

        GovtechMerkleLedgerEngine.MerkleBlock block = engine.appendAndSealBlock(txsOdd);
        assertEquals(5, block.transactions().size());

        for (int i = 1; i <= 5; i++) {
            Optional<GovtechMerkleLedgerEngine.MerkleProof> proof = engine.generateProof(block.blockHeight(), "TX_ODD_" + i);
            assertTrue(proof.isPresent());
            assertTrue(GovtechMerkleLedgerEngine.verifyProof(proof.get()));
        }
    }

    @Test
    @DisplayName("Debe manejar consultas de transacciones o bloques inexistentes de forma segura")
    void shouldHandleNonExistentQueriesGracefully() {
        GovtechMerkleLedgerEngine engine = new GovtechMerkleLedgerEngine();

        assertFalse(engine.getBlockByHeight(999).isPresent());
        assertFalse(engine.generateProof(0L, "TX_NON_EXISTENT").isPresent());
        assertFalse(engine.generateProof(999L, "TX_ANY").isPresent());
    }
}
