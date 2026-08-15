package com.pct.alert;

import com.corp.contracts.SystemAlertRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AlertAggregator - Pruebas TDD de Agregación y Deduplicación")
class AlertAggregatorTest {

    @Test
    @DisplayName("AlertRingBuffer mantiene el límite de capacidad O(1) sobreescribiendo los más antiguos")
    void testRingBufferBoundedCapacity() {
        AlertRingBuffer buffer = new AlertRingBuffer(3);

        buffer.push(new SystemAlertRecord("a-1", "INFO", "service-a", "cell-1", "Msg 1", 1000L));
        buffer.push(new SystemAlertRecord("a-2", "INFO", "service-a", "cell-1", "Msg 2", 2000L));
        buffer.push(new SystemAlertRecord("a-3", "INFO", "service-a", "cell-1", "Msg 3", 3000L));
        buffer.push(new SystemAlertRecord("a-4", "INFO", "service-a", "cell-1", "Msg 4", 4000L));

        assertEquals(3, buffer.size());
        List<SystemAlertRecord> snapshot = buffer.snapshot();
        assertEquals(3, snapshot.size());
        assertEquals("a-2", snapshot.get(0).alertId());
        assertEquals("a-4", snapshot.get(2).alertId());
    }

    @Test
    @DisplayName("SlidingWindowDeduplicator descarta duplicados en la ventana temporal")
    void testDeduplicatorWindow() {
        SlidingWindowDeduplicator deduplicator = new SlidingWindowDeduplicator(5000L); // 5 segundos
        SystemAlertRecord alert = new SystemAlertRecord("a-100", "WARNING", "battery-monitor", "cell-88", "Batería baja en sensor", System.currentTimeMillis());

        assertTrue(deduplicator.isUnique(alert));
        assertFalse(deduplicator.isUnique(alert)); // Duplicado inmediato descartado
    }

    @Test
    @DisplayName("AlertAggregatorService enruta alertas críticas a la cola de alta prioridad")
    void testAggregatorPriorityRouting() {
        AlertAggregatorService service = new AlertAggregatorService(50, 10000L);

        SystemAlertRecord infoAlert = new SystemAlertRecord("a-1", "INFO", "weather-svc", "cell-1", "Viento moderado", System.currentTimeMillis());
        SystemAlertRecord criticalAlert = new SystemAlertRecord("a-2", "CRITICAL", "grid-svc", "cell-1", "Caída de tensión severa", System.currentTimeMillis());

        assertTrue(service.submitAlert(infoAlert));
        assertTrue(service.submitAlert(criticalAlert));

        SystemAlertRecord highPriority = service.pollHighPriority();
        assertNotNull(highPriority);
        assertEquals("a-2", highPriority.alertId());
        assertNull(service.pollHighPriority()); // No hay más críticas
    }
}
