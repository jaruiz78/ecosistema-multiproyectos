package com.pct.sync.crdt;

import java.io.Serializable;
import java.util.Objects;

/**
 * LwwRegister (Last-Write-Wins Register)
 * Estructura CRDT basada en estado donde la última escritura (por timestamp monótono) prevalece.
 *
 * @param <T> Tipo del valor contenido
 */
public final class LwwRegister<T> implements Serializable {
    private final T value;
    private final long timestampEpochMs;
    private final String nodeId;

    public LwwRegister(T value, long timestampEpochMs, String nodeId) {
        this.value = value;
        this.timestampEpochMs = timestampEpochMs;
        this.nodeId = Objects.requireNonNull(nodeId, "nodeId no puede ser nulo");
    }

    public static <T> LwwRegister<T> of(T value, String nodeId) {
        return new LwwRegister<>(value, System.currentTimeMillis(), nodeId);
    }

    public T getValue() {
        return value;
    }

    public long getTimestampEpochMs() {
        return timestampEpochMs;
    }

    public String getNodeId() {
        return nodeId;
    }

    /**
     * Merge determinista: Conmutativo, asociativo e idempotente (Semilattice Join).
     */
    public LwwRegister<T> merge(LwwRegister<T> other) {
        if (other == null) {
            return this;
        }
        if (other.timestampEpochMs > this.timestampEpochMs) {
            return other;
        } else if (other.timestampEpochMs < this.timestampEpochMs) {
            return this;
        } else {
            // Desempate determinista por nodeId
            return this.nodeId.compareTo(other.nodeId) >= 0 ? this : other;
        }
    }
}
