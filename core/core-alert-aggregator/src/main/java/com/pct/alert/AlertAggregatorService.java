package com.pct.alert;

import com.corp.contracts.SystemAlertRecord;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AlertAggregatorService
 * Orquestador de agregación, deduplicación y despacho de alertas con Virtual Threads en Java 25.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_2_go_y_concurrencia">FACULTAD_IV: Concurrencia Go CSP & Ring-Buffers</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class AlertAggregatorService {

    private final AlertRingBuffer ringBuffer;
    private final SlidingWindowDeduplicator deduplicator;
    private final BlockingQueue<SystemAlertRecord> highPriorityQueue = new LinkedBlockingQueue<>(1000);
    private final AtomicLong ingestedCount = new AtomicLong(0);
    private final AtomicLong droppedDuplicates = new AtomicLong(0);

    public AlertAggregatorService(int bufferCapacity, long deduplicationWindowMs) {
        this.ringBuffer = new AlertRingBuffer(bufferCapacity);
        this.deduplicator = new SlidingWindowDeduplicator(deduplicationWindowMs);
    }

    public boolean submitAlert(SystemAlertRecord alert) {
        ingestedCount.incrementAndGet();

        if (!deduplicator.isUnique(alert)) {
            droppedDuplicates.incrementAndGet();
            return false;
        }

        ringBuffer.push(alert);

        if ("CRITICAL".equalsIgnoreCase(alert.severity()) || "EMERGENCY".equalsIgnoreCase(alert.severity())) {
            highPriorityQueue.offer(alert);
        }

        return true;
    }

    public List<SystemAlertRecord> getRecentAlerts() {
        return ringBuffer.snapshot();
    }

    public SystemAlertRecord pollHighPriority() {
        return highPriorityQueue.poll();
    }

    public long getIngestedCount() {
        return ingestedCount.get();
    }

    public long getDroppedDuplicates() {
        return droppedDuplicates.get();
    }
}
