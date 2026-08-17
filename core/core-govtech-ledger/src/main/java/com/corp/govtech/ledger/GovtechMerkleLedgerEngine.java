package com.corp.govtech.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Motor Criptográfico de Grado Enterprise para Libro Mayor GovTech y Licitaciones Públicas.
 * Implementa árboles de Merkle binarios balanceados, encadenamiento inmutable de bloques SHA-256
 * y verificación formal de pruebas de inclusión (Merkle Proofs) en O(log N).
 *
 * <p>Diseño sin dependencias externas, 100% puro Java 25, libre de reflexión dinámica y
 * optimizado para Virtual Threads (Project Loom) con exclusión mutua mediante {@link ReentrantLock}.</p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR-001: Loom Anti-Pinning</a>
 * @reference Merkle (1987) A Digital Signature Based on a Conventional Encryption Function; Nakamoto (2008)
 */
public final class GovtechMerkleLedgerEngine {

    private static final String SHA_256 = "SHA-256";
    private static final HexFormat HEX = HexFormat.of();
    private static final String GENESIS_PREVIOUS_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    private final ReentrantLock lock = new ReentrantLock();
    private final List<MerkleBlock> blockchain = new ArrayList<>();
    private final Map<String, MerkleBlock> blockIndex = new ConcurrentHashMap<>();

    public GovtechMerkleLedgerEngine() {
        // Inicializar bloque Génesis
        sealGenesisBlock();
    }

    /**
     * Registro inmutable de una transacción dentro del libro mayor.
     *
     * @param transactionId Identificador único unívoco (UUID o Hash)
     * @param tenantId      Identificador del Tenant soberano
     * @param payload       Cuerpo serializado canónico de la licitación o evento
     * @param timestamp     Marca temporal Unix Epoch en milisegundos
     */
    public record LedgerTransaction(
            String transactionId,
            String tenantId,
            String payload,
            long timestamp
    ) {
        public LedgerTransaction {
            Objects.requireNonNull(transactionId, "transactionId no puede ser nulo");
            Objects.requireNonNull(tenantId, "tenantId no puede ser nulo");
            Objects.requireNonNull(payload, "payload no puede ser nulo");
            if (timestamp < 0) {
                throw new IllegalArgumentException("timestamp debe ser no-negativo (Hoare Precondition)");
            }
        }

        /**
         * Calcula el hash SHA-256 determinista de la transacción en O(1).
         */
        public String calculateHash() {
            String canonical = transactionId + ":" + tenantId + ":" + timestamp + ":" + payload;
            return GovtechMerkleLedgerEngine.sha256Hex(canonical);
        }
    }

    /**
     * Paso de prueba de Merkle (nodo hermano y posición izquierda/derecha).
     */
    public record MerkleProofStep(String siblingHash, boolean isLeft) {
        public MerkleProofStep {
            Objects.requireNonNull(siblingHash, "siblingHash no puede ser nulo");
        }
    }

    /**
     * Prueba de inclusión de Merkle que certifica la pertenencia de una transacción en O(log N).
     */
    public record MerkleProof(
            String transactionHash,
            String merkleRoot,
            long blockHeight,
            List<MerkleProofStep> steps
    ) {
        public MerkleProof {
            Objects.requireNonNull(transactionHash, "transactionHash no puede ser nulo");
            Objects.requireNonNull(merkleRoot, "merkleRoot no puede ser nulo");
            Objects.requireNonNull(steps, "steps no puede ser nulo");
            if (blockHeight < 0) {
                throw new IllegalArgumentException("blockHeight debe ser no-negativo");
            }
        }
    }

    /**
     * Bloque inmutable sellado criptográficamente en la cadena de bloques.
     */
    public record MerkleBlock(
            long blockHeight,
            long timestamp,
            String previousHash,
            String merkleRoot,
            String blockHash,
            List<LedgerTransaction> transactions
    ) {
        public MerkleBlock {
            Objects.requireNonNull(previousHash, "previousHash no puede ser nulo");
            Objects.requireNonNull(merkleRoot, "merkleRoot no puede ser nulo");
            Objects.requireNonNull(blockHash, "blockHash no puede ser nulo");
            Objects.requireNonNull(transactions, "transactions no puede ser nulo");
            transactions = List.copyOf(transactions); // Garantizar inmutabilidad profunda
        }
    }

    /**
     * Sella el bloque Génesis inicial de forma determinista.
     */
    private void sealGenesisBlock() {
        lock.lock();
        try {
            if (!blockchain.isEmpty()) {
                return;
            }
            LedgerTransaction genesisTx = new LedgerTransaction(
                    "GENESIS_TX_0",
                    "SYSTEM",
                    "GENESIS_BLOCK_GOVTECH_LEDGER",
                    0L
            );
            List<LedgerTransaction> txs = List.of(genesisTx);
            String merkleRoot = computeMerkleRoot(txs);
            String blockHash = computeBlockHash(0L, 0L, GENESIS_PREVIOUS_HASH, merkleRoot);
            MerkleBlock genesis = new MerkleBlock(0L, 0L, GENESIS_PREVIOUS_HASH, merkleRoot, blockHash, txs);
            blockchain.add(genesis);
            blockIndex.put(blockHash, genesis);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Agrega un nuevo bloque de transacciones y lo sella criptográficamente en O(N).
     *
     * @param transactions Lista de transacciones a incorporar en el bloque.
     * @return El bloque sellado inmutable.
     */
    public MerkleBlock appendAndSealBlock(List<LedgerTransaction> transactions) {
        Objects.requireNonNull(transactions, "La lista de transacciones no puede ser nula");
        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("No se pueden sellar bloques vacíos");
        }

        lock.lock();
        try {
            MerkleBlock previousBlock = blockchain.getLast();
            long newHeight = previousBlock.blockHeight() + 1;
            long currentTimestamp = Instant.now().toEpochMilli();
            String merkleRoot = computeMerkleRoot(transactions);
            String blockHash = computeBlockHash(newHeight, currentTimestamp, previousBlock.blockHash(), merkleRoot);

            MerkleBlock newBlock = new MerkleBlock(
                    newHeight,
                    currentTimestamp,
                    previousBlock.blockHash(),
                    merkleRoot,
                    blockHash,
                    transactions
            );

            blockchain.add(newBlock);
            blockIndex.put(blockHash, newBlock);
            return newBlock;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Calcula la raíz de Merkle binaria balanceada a partir de una lista de transacciones en O(N).
     */
    public static String computeMerkleRoot(List<LedgerTransaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return GENESIS_PREVIOUS_HASH;
        }
        List<String> currentLevel = new ArrayList<>(transactions.stream().map(LedgerTransaction::calculateHash).toList());

        while (currentLevel.size() > 1) {
            List<String> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                String left = currentLevel.get(i);
                String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left; // Duplicar impar
                nextLevel.add(hashPair(left, right));
            }
            currentLevel = nextLevel;
        }
        return currentLevel.getFirst();
    }

    /**
     * Genera una prueba de inclusión de Merkle para una transacción específica dentro de un bloque en O(log N).
     */
    public Optional<MerkleProof> generateProof(long blockHeight, String transactionId) {
        lock.lock();
        try {
            if (blockHeight < 0 || blockHeight >= blockchain.size()) {
                return Optional.empty();
            }
            MerkleBlock block = blockchain.get((int) blockHeight);
            List<LedgerTransaction> txs = block.transactions();

            int targetIndex = -1;
            for (int i = 0; i < txs.size(); i++) {
                if (txs.get(i).transactionId().equals(transactionId)) {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex == -1) {
                return Optional.empty();
            }

            String targetTxHash = txs.get(targetIndex).calculateHash();
            List<MerkleProofStep> steps = new ArrayList<>();
            List<String> currentLevel = new ArrayList<>(txs.stream().map(LedgerTransaction::calculateHash).toList());
            int currentIndex = targetIndex;

            while (currentLevel.size() > 1) {
                int pairIndex = (currentIndex % 2 == 0) ? currentIndex + 1 : currentIndex - 1;
                if (pairIndex >= currentLevel.size()) {
                    pairIndex = currentIndex; // Caso impar duplicado
                }

                String siblingHash = currentLevel.get(pairIndex);
                boolean siblingIsLeft = (pairIndex < currentIndex);
                steps.add(new MerkleProofStep(siblingHash, siblingIsLeft));

                List<String> nextLevel = new ArrayList<>();
                for (int i = 0; i < currentLevel.size(); i += 2) {
                    String left = currentLevel.get(i);
                    String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left;
                    nextLevel.add(hashPair(left, right));
                }
                currentLevel = nextLevel;
                currentIndex /= 2;
            }

            return Optional.of(new MerkleProof(targetTxHash, block.merkleRoot(), blockHeight, steps));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Verifica formalmente la validez matemática de una prueba de Merkle en O(log N).
     */
    public static boolean verifyProof(MerkleProof proof) {
        if (proof == null || proof.steps() == null) {
            return false;
        }
        String currentHash = proof.transactionHash();
        for (MerkleProofStep step : proof.steps()) {
            if (step.isLeft()) {
                currentHash = hashPair(step.siblingHash(), currentHash);
            } else {
                currentHash = hashPair(currentHash, step.siblingHash());
            }
        }
        return currentHash.equalsIgnoreCase(proof.merkleRoot());
    }

    /**
     * Valida la integridad criptográfica completa de la cadena de bloques en O(B * N).
     */
    public boolean validateChainIntegrity() {
        lock.lock();
        try {
            for (int i = 1; i < blockchain.size(); i++) {
                MerkleBlock current = blockchain.get(i);
                MerkleBlock previous = blockchain.get(i - 1);

                // 1. Validar encadenamiento de hash previo
                if (!current.previousHash().equals(previous.blockHash())) {
                    return false;
                }

                // 2. Validar consistencia de la raíz de Merkle recalculada
                String calculatedMerkleRoot = computeMerkleRoot(current.transactions());
                if (!current.merkleRoot().equals(calculatedMerkleRoot)) {
                    return false;
                }

                // 3. Validar hash del bloque actual
                String calculatedBlockHash = computeBlockHash(
                        current.blockHeight(),
                        current.timestamp(),
                        current.previousHash(),
                        current.merkleRoot()
                );
                if (!current.blockHash().equals(calculatedBlockHash)) {
                    return false;
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int getChainLength() {
        lock.lock();
        try {
            return blockchain.size();
        } finally {
            lock.unlock();
        }
    }

    public Optional<MerkleBlock> getBlockByHeight(long height) {
        lock.lock();
        try {
            if (height < 0 || height >= blockchain.size()) {
                return Optional.empty();
            }
            return Optional.of(blockchain.get((int) height));
        } finally {
            lock.unlock();
        }
    }

    // ==========================================
    // UTILIDADES CRIPTOGRÁFICAS PURAS (O(1))
    // ==========================================

    private static String hashPair(String left, String right) {
        return sha256Hex(left + right);
    }

    private static String computeBlockHash(long height, long timestamp, String prevHash, String merkleRoot) {
        String payload = height + ":" + timestamp + ":" + prevHash + ":" + merkleRoot;
        return sha256Hex(payload);
    }

    public static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HEX.formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no soportado en JVM", e);
        }
    }
}
