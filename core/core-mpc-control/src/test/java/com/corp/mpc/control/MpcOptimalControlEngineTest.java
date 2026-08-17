package com.corp.mpc.control;

import com.corp.mpc.control.domain.MpcBounds;
import com.corp.mpc.control.domain.MpcControlAction;
import com.corp.mpc.control.domain.MpcState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link MpcOptimalControlEngine}.
 */
class MpcOptimalControlEngineTest {

    @Test
    @DisplayName("Debe converger hacia la referencia en un sistema lineal de 2 estados y 1 control")
    void shouldConvergeTowardsReferenceIn2DSystem() {
        // Sistema: x_{k+1} = [[1, 0.1], [0, 0.95]] x_k + [[0], [0.1]] u_k
        int horizon = 10;
        double[][] A = {
                {1.0, 0.1},
                {0.0, 0.95}
        };
        double[][] B = {
                {0.0},
                {0.1}
        };
        double[] diagQ = {10.0, 1.0};
        double[] diagR = {0.1};

        MpcOptimalControlEngine engine = new MpcOptimalControlEngine(
                horizon, A, B, diagQ, diagR, 100, 1e-4
        );

        MpcState initialState = new MpcState(new double[]{0.0, 0.0}, System.currentTimeMillis(), "TENANT_VPP_01");
        double[] reference = new double[]{10.0, 0.0};
        MpcBounds bounds = new MpcBounds(new double[]{-5.0}, new double[]{5.0});

        MpcControlAction action = engine.solve(initialState, reference, bounds);

        assertNotNull(action);
        assertEquals(1, action.optimalControl().length);
        // Debe empujar con control positivo hacia la referencia
        assertTrue(action.optimalControl()[0] > 0.0);
        assertTrue(action.optimalControl()[0] <= 5.0);
        assertTrue(action.totalCost() > 0.0);
    }

    @Test
    @DisplayName("Debe respetar los límites de caja estrictos en el control")
    void shouldRespectBoxConstraintsStrictly() {
        int horizon = 5;
        double[][] A = {{1.0}};
        double[][] B = {{1.0}};
        double[] diagQ = {100.0};
        double[] diagR = {0.01};

        MpcOptimalControlEngine engine = new MpcOptimalControlEngine(
                horizon, A, B, diagQ, diagR, 50, 1e-4
        );

        MpcState state = new MpcState(new double[]{0.0}, 0L, "TENANT_WATER_01");
        double[] reference = new double[]{1000.0}; // Gran salto que saturaría el control
        MpcBounds bounds = new MpcBounds(new double[]{-2.0}, new double[]{2.0});

        MpcControlAction action = engine.solve(state, reference, bounds);

        assertEquals(2.0, action.optimalControl()[0], 1e-5);
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia con Virtual Threads sin degradación")
    void shouldHandleHighConcurrencyWithVirtualThreads() throws Exception {
        int horizon = 5;
        double[][] A = {{1.0}};
        double[][] B = {{1.0}};
        double[] diagQ = {1.0};
        double[] diagR = {1.0};

        MpcOptimalControlEngine engine = new MpcOptimalControlEngine(
                horizon, A, B, diagQ, diagR, 20, 1e-3
        );

        int totalThreads = 50;
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < totalThreads; i++) {
                final double target = i * 2.0;
                futures.add(executor.submit(() -> {
                    MpcState state = new MpcState(new double[]{0.0}, System.currentTimeMillis(), "TENANT_CONC");
                    MpcBounds bounds = new MpcBounds(new double[]{-10.0}, new double[]{10.0});
                    MpcControlAction act = engine.solve(state, new double[]{target}, bounds);
                    assertNotNull(act);
                }));
            }
            for (var f : futures) {
                f.get();
            }
        }
    }
}
