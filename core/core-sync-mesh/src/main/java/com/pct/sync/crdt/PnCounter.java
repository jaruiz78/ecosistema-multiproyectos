package com.pct.sync.crdt;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PnCounter (Positive-Negative Counter)
 * Contador CRDT sin conflictos que admite incrementos y decrementos concurrentes.
 */
public final class PnCounter implements Serializable {
    private final Map<String, Long> pMap;
    private final Map<String, Long> nMap;

    public PnCounter() {
        this.pMap = new HashMap<>();
        this.nMap = new HashMap<>();
    }

    private PnCounter(Map<String, Long> pMap, Map<String, Long> nMap) {
        this.pMap = new HashMap<>(pMap);
        this.nMap = new HashMap<>(nMap);
    }

    public PnCounter increment(String nodeId, long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("delta debe ser positivo: " + delta);
        }
        Objects.requireNonNull(nodeId, "nodeId requerido");
        Map<String, Long> newP = new HashMap<>(this.pMap);
        newP.put(nodeId, newP.getOrDefault(nodeId, 0L) + delta);
        return new PnCounter(newP, this.nMap);
    }

    public PnCounter decrement(String nodeId, long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("delta debe ser positivo: " + delta);
        }
        Objects.requireNonNull(nodeId, "nodeId requerido");
        Map<String, Long> newN = new HashMap<>(this.nMap);
        newN.put(nodeId, newN.getOrDefault(nodeId, 0L) + delta);
        return new PnCounter(this.pMap, newN);
    }

    public long value() {
        long pSum = pMap.values().stream().mapToLong(Long::longValue).sum();
        long nSum = nMap.values().stream().mapToLong(Long::longValue).sum();
        return pSum - nSum;
    }

    public Map<String, Long> getPMap() {
        return Collections.unmodifiableMap(pMap);
    }

    public Map<String, Long> getNMap() {
        return Collections.unmodifiableMap(nMap);
    }

    /**
     * Merge determinista de dos PnCounters (Máximo por réplica).
     */
    public PnCounter merge(PnCounter other) {
        if (other == null) return this;

        Map<String, Long> mergedP = new HashMap<>(this.pMap);
        for (Map.Entry<String, Long> entry : other.pMap.entrySet()) {
            mergedP.put(entry.getKey(), Math.max(mergedP.getOrDefault(entry.getKey(), 0L), entry.getValue()));
        }

        Map<String, Long> mergedN = new HashMap<>(this.nMap);
        for (Map.Entry<String, Long> entry : other.nMap.entrySet()) {
            mergedN.put(entry.getKey(), Math.max(mergedN.getOrDefault(entry.getKey(), 0L), entry.getValue()));
        }

        return new PnCounter(mergedP, mergedN);
    }
}
