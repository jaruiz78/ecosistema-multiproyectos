package com.pct.alert;

import com.corp.contracts.SystemAlertRecord;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SlidingWindowDeduplicator
 * Filtro de deduplicación O(1) con ventana temporal deslizante (TTL).
 * Descarta alertas idénticas ocurridas dentro del margen temporal configurado.
 */
public final class SlidingWindowDeduplicator {

    private final long windowDurationMs;
    private final ConcurrentHashMap<String, Long> lastSeenMap = new ConcurrentHashMap<>();

    public SlidingWindowDeduplicator(long windowDurationMs) {
        if (windowDurationMs <= 0) {
            throw new IllegalArgumentException("windowDurationMs debe ser positivo");
        }
        this.windowDurationMs = windowDurationMs;
    }

    /**
     * Evalúa si una alerta es nueva dentro de la ventana de deduplicación.
     * @return true si es única (debe procesarse), false si es un duplicado reciente.
     */
    public boolean isUnique(SystemAlertRecord alert) {
        String fingerprint = generateFingerprint(alert);
        long now = System.currentTimeMillis();

        Long previous = lastSeenMap.put(fingerprint, now);
        if (previous == null) {
            return true;
        }

        // Si la alerta previa ocurrió fuera de la ventana, es aceptada
        return (now - previous) >= windowDurationMs;
    }

    public void cleanExpiredEntries() {
        long now = System.currentTimeMillis();
        lastSeenMap.entrySet().removeIf(entry -> (now - entry.getValue()) >= windowDurationMs);
    }

    private String generateFingerprint(SystemAlertRecord alert) {
        return alert.originService() + ":" + alert.severity() + ":" + alert.message().hashCode();
    }
}
