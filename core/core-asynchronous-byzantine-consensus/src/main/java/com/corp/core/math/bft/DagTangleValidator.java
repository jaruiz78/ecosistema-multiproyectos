package com.corp.core.math.bft;

import java.io.Serializable;
import java.util.List;

/**
 * Validador de topologías de consenso sobre Grafos Acíclicos Dirigidos (DAG-Tangle).
 * Verifica que cada nueva transacción referencie y valide al menos dos transacciones previas (tips).
 */
public record DagTangleValidator() implements Serializable {

    public record TangleTransaction(
            String txId,
            List<String> referencedParentTxIds,
            long weight
    ) implements Serializable {}

    public static boolean validateDagAttachment(TangleTransaction tx) {
        if (tx == null || tx.txId() == null || tx.referencedParentTxIds() == null) {
            return false;
        }
        // Debe validar al menos 2 transacciones anteriores
        return tx.referencedParentTxIds().size() >= 2 && !tx.referencedParentTxIds().contains(tx.txId());
    }
}
