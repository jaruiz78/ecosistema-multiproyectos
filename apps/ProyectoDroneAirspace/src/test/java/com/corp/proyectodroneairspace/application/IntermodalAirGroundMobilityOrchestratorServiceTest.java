package com.corp.proyectodroneairspace.application;

import com.corp.proyectodroneairspace.domain.IntermodalMobilityPlan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas para {@link IntermodalAirGroundMobilityOrchestratorService}.
 * Verifica integración 3D H3 + NATS Event Mesh y ejecución concurrente bajo Virtual Threads.
 */
class IntermodalAirGroundMobilityOrchestratorServiceTest {

    private final IntermodalAirGroundMobilityOrchestratorService service =
            new IntermodalAirGroundMobilityOrchestratorService();

    @Test
    @DisplayName("Debe planificar y publicar una ruta intermodal 3D en NATS exitosamente")
    void shouldPlanAndPublishIntermodalRoute() {
        String originH3 = "88390884d5fffff";
        String destH3 = "88390884d1fffff";

        IntermodalMobilityPlan plan = service.planIntermodalRoute(
                originH3, 36.7213, -4.4214, 20.0, // Málaga Centro
                destH3, 36.7500, -4.4000, 150.0,  // Hub Logístico Norte
                120                               // 120m techo de vuelo
        );

        assertNotNull(plan);
        assertEquals(originH3, plan.originH3Index());
        assertEquals(destH3, plan.destinationH3Index());
        assertTrue(plan.totalDistance3Dkm() > 0, "La distancia 3D debe ser positiva");
        assertTrue(plan.droneEstimatedMinutes() > 0);
        assertTrue(plan.groundEstimatedMinutes() > 0);
        assertTrue(plan.natsEventPublished(), "El evento debe haber sido publicado en la malla NATS");
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia con 40 Virtual Threads sin contención de hilos")
    void shouldExecuteConcurrentlyUnderVirtualThreads() throws InterruptedException {
        int threads = 40;
        AtomicInteger successCounter = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                final double deltaLat = i * 0.001;
                executor.submit(() -> {
                    IntermodalMobilityPlan plan = service.planIntermodalRoute(
                            "88390884d5fffff", 36.7213 + deltaLat, -4.4214, 20.0,
                            "88390884d1fffff", 36.7500, -4.4000, 100.0,
                            80
                    );
                    if (plan.natsEventPublished() && plan.totalDistance3Dkm() > 0) {
                        successCounter.incrementAndGet();
                    }
                });
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        assertEquals(threads, successCounter.get(), "Todos los Virtual Threads deben despachar y publicar eventos");
    }
}
