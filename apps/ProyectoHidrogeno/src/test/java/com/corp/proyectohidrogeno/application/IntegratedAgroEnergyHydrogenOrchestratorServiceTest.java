package com.corp.proyectohidrogeno.application;

import com.corp.proyectohidrogeno.domain.AgroEnergyHydrogenDispatchPlan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas para {@link IntegratedAgroEnergyHydrogenOrchestratorService}.
 * Verifica la orquestación conjunta MPC + Verificación Formal Hoare y concurrencia Virtual Threads.
 */
class IntegratedAgroEnergyHydrogenOrchestratorServiceTest {

    private final IntegratedAgroEnergyHydrogenOrchestratorService orchestrator =
            new IntegratedAgroEnergyHydrogenOrchestratorService();

    @Test
    @DisplayName("Debe generar un plan de despacho óptimo verificado formalmente")
    void shouldGenerateVerifiedDispatchPlan() {
        double solarKw = 500.0;
        double waterReserveM3 = 200.0;
        double targetH2Kg = 8.0;

        AgroEnergyHydrogenDispatchPlan plan = orchestrator.orchestrateDispatch(solarKw, waterReserveM3, targetH2Kg);

        assertNotNull(plan);
        assertTrue(plan.isVerified(), "El plan debe estar formalmente certificado por el verificador");
        assertTrue(plan.allocatedElectrolyzerKw() > 0, "Debe asignar potencia al electrolizador");
        assertTrue(plan.allocatedIrrigationKw() >= 0, "Debe asignar potencia al regadío");
        assertTrue(plan.allocatedElectrolyzerKw() + plan.allocatedIrrigationKw() <= solarKw,
                "La potencia total asignada no puede superar la generación solar");
        assertTrue(plan.expectedHydrogenKgPerHour() > 0);
        assertNotNull(plan.formalProofDigest());
    }

    @Test
    @DisplayName("Debe rechazar parámetros de entrada negativos")
    void shouldRejectNegativeInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                orchestrator.orchestrateDispatch(-100.0, 50.0, 5.0));
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia bajo Virtual Threads Loom")
    void shouldExecuteConcurrentlyUnderVirtualThreads() throws InterruptedException {
        int threads = 30;
        AtomicInteger successCounter = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                final double solar = 200.0 + (i * 10.0);
                executor.submit(() -> {
                    AgroEnergyHydrogenDispatchPlan plan = orchestrator.orchestrateDispatch(solar, 100.0, 4.0);
                    if (plan.isVerified() && plan.expectedHydrogenKgPerHour() > 0) {
                        successCounter.incrementAndGet();
                    }
                });
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        assertEquals(threads, successCounter.get(), "Todos los Virtual Threads deben orquestar planes verificados");
    }
}
