package com.pct.sync;

import com.corp.contracts.DifferentialSyncPatch;
import com.pct.sync.crdt.LwwRegister;
import com.pct.sync.crdt.OrSet;
import com.pct.sync.crdt.PnCounter;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SyncMeshEngine
 * Motor de sincronización offline bidireccional que gestiona réplicas CRDT por tenant y recurso.
 * Proporciona convergencia determinista O(1) ante desconexiones intermitentes.
 */
public class SyncMeshEngine {

    private final String localNodeId;
    private final ConcurrentHashMap<String, LwwRegister<String>> lwwStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PnCounter> counterStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, OrSet<String>> setStore = new ConcurrentHashMap<>();
    private final AtomicLong vectorClock = new AtomicLong(0);

    public SyncMeshEngine(String localNodeId) {
        this.localNodeId = localNodeId != null ? localNodeId : "node-" + System.currentTimeMillis();
    }

    public String getLocalNodeId() {
        return localNodeId;
    }

    // --- LWW Register ---
    public void updateLww(String entityKey, String value) {
        long ts = System.currentTimeMillis();
        lwwStore.compute(entityKey, (k, existing) -> {
            LwwRegister<String> next = new LwwRegister<>(value, ts, localNodeId);
            return existing == null ? next : existing.merge(next);
        });
        vectorClock.incrementAndGet();
    }

    public String getLwwValue(String entityKey) {
        LwwRegister<String> reg = lwwStore.get(entityKey);
        return reg != null ? reg.getValue() : null;
    }

    // --- PN Counter ---
    public void incrementCounter(String entityKey, long delta) {
        counterStore.compute(entityKey, (k, existing) -> {
            PnCounter base = existing == null ? new PnCounter() : existing;
            return base.increment(localNodeId, delta);
        });
        vectorClock.incrementAndGet();
    }

    public void decrementCounter(String entityKey, long delta) {
        counterStore.compute(entityKey, (k, existing) -> {
            PnCounter base = existing == null ? new PnCounter() : existing;
            return base.decrement(localNodeId, delta);
        });
        vectorClock.incrementAndGet();
    }

    public long getCounterValue(String entityKey) {
        PnCounter counter = counterStore.get(entityKey);
        return counter != null ? counter.value() : 0L;
    }

    // --- OR-Set ---
    public void addToSet(String entityKey, String element) {
        setStore.compute(entityKey, (k, existing) -> {
            OrSet<String> base = existing == null ? new OrSet<>() : existing;
            return base.add(element);
        });
        vectorClock.incrementAndGet();
    }

    public void removeFromSet(String entityKey, String element) {
        setStore.compute(entityKey, (k, existing) -> {
            if (existing == null) return new OrSet<>();
            return existing.remove(element);
        });
        vectorClock.incrementAndGet();
    }

    public boolean isInSet(String entityKey, String element) {
        OrSet<String> set = setStore.get(entityKey);
        return set != null && set.contains(element);
    }

    // --- Sincronización Diferencial ---
    public DifferentialSyncPatch exportLwwPatch(String tenantId, String entityKey) throws IOException {
        LwwRegister<String> reg = lwwStore.get(entityKey);
        byte[] payload = serialize(reg);
        return new DifferentialSyncPatch(entityKey, tenantId, vectorClock.get(), "LWW_REGISTER", payload, System.currentTimeMillis());
    }

    @SuppressWarnings("unchecked")
    public void applyLwwPatch(DifferentialSyncPatch patch) throws IOException, ClassNotFoundException {
        if (!"LWW_REGISTER".equals(patch.crdtType())) {
            throw new IllegalArgumentException("Tipo CRDT no coincide: " + patch.crdtType());
        }
        LwwRegister<String> incoming = (LwwRegister<String>) deserialize(patch.payload());
        if (incoming != null) {
            lwwStore.compute(patch.entityId(), (k, existing) -> existing == null ? incoming : existing.merge(incoming));
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        if (obj == null) return new byte[0];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null || bytes.length == 0) return null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
}
