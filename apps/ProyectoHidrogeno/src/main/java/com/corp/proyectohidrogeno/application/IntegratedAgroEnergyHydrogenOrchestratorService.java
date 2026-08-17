package com.corp.proyectohidrogeno.application;

import com.corp.formal.verification.HoareInvariantVerifier;
import com.corp.formal.verification.domain.HoareTriple;
import com.corp.formal.verification.domain.StateInvariant;
import com.corp.formal.verification.domain.VerificationCertificate;
import com.corp.mpc.control.MpcOptimalControlEngine;
import com.corp.mpc.control.domain.MpcBounds;
import com.corp.mpc.control.domain.MpcControlAction;
import com.corp.mpc.control.domain.MpcState;
import com.corp.proyectohidrogeno.domain.AgroEnergyHydrogenDispatchPlan;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio Orquestador de Sinergia Cruzada Agro-Energía-Hidrógeno.
 *
 * <p>Integra:
 * <ul>
 *   <li>Control predictivo cuadrático (MPC) para despacho óptimo de potencia renovable.</li>
 *   <li>Verificación formal axiomática (Lógica de Hoare) para conservación de masa y energía.</li>
 * </ul>
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 * @see docs/adr/adr-020-model-predictive-control-quadratic-optimization.md
 * @see docs/adr/adr-021-formal-verification-hoare-logic-inductive-invariants.md
 */
public final class IntegratedAgroEnergyHydrogenOrchestratorService {

    private final HoareInvariantVerifier formalVerifier;
    private final ReentrantLock lock = new ReentrantLock();

    public IntegratedAgroEnergyHydrogenOrchestratorService(HoareInvariantVerifier formalVerifier) {
        this.formalVerifier = Objects.requireNonNull(formalVerifier, "formalVerifier no puede ser nulo");
    }

    public IntegratedAgroEnergyHydrogenOrchestratorService() {
        this(new HoareInvariantVerifier());
    }

    /**
     * Calcula y certifica formalmente el despacho óptimo conjunto para regadío y producción de H2.
     *
     * @param solarGenerationKw Generación solar total disponible
     * @param waterReserveM3    Volumen de agua disponible en la balsa/pozo
     * @param targetHydrogenKg  Meta de producción de hidrógeno
     * @return {@link AgroEnergyHydrogenDispatchPlan}
     */
    public AgroEnergyHydrogenDispatchPlan orchestrateDispatch(
            double solarGenerationKw,
            double waterReserveM3,
            double targetHydrogenKg
    ) {
        if (solarGenerationKw < 0 || waterReserveM3 < 0 || targetHydrogenKg < 0) {
            throw new IllegalArgumentException("Parámetros de entrada deben ser no negativos");
        }

        lock.lock();
        try {
            // 1. Configurar y resolver optimización MPC
            double[][] A = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
            double[][] B = new double[][]{{1.0 / 50.0, 0.0}, {-0.000009, -0.00005}};
            double[] Q = new double[]{2.0, 0.5};
            double[] R = new double[]{0.1, 0.1};

            MpcOptimalControlEngine mpcEngine = new MpcOptimalControlEngine(
                    5, A, B, Q, R, 20, 0.05
            );

            double[] x0 = new double[]{0.0, waterReserveM3};
            double[] xRef = new double[]{targetHydrogenKg, waterReserveM3 * 0.9};
            MpcState initialState = new MpcState(x0, System.currentTimeMillis(), "tenant-agro-h2");
            MpcBounds bounds = new MpcBounds(
                    new double[]{0.0, 0.0},
                    new double[]{Math.max(1.0, solarGenerationKw * 0.85), Math.max(1.0, solarGenerationKw * 0.40)}
            );

            MpcControlAction optimalAction = mpcEngine.solve(initialState, xRef, bounds);

            double allocatedH2Kw = Math.min(solarGenerationKw, Math.max(0.0, optimalAction.optimalControl()[0]));
            double remainingSolarKw = Math.max(0.0, solarGenerationKw - allocatedH2Kw);
            double allocatedIrrigationKw = Math.min(remainingSolarKw, Math.max(0.0, optimalAction.optimalControl()[1]));

            // Rendimiento electrolizador PEM: ~50 kWh / kg H2, consumo de agua pura: ~9 L / kg H2
            double expectedH2KgPerHour = allocatedH2Kw / 50.0;
            double waterForH2Liters = expectedH2KgPerHour * 9.0;
            double waterForIrrigationLiters = allocatedIrrigationKw * 100.0;
            double totalWaterLitersPerHour = waterForH2Liters + waterForIrrigationLiters;

            // 2. Verificación Formal de Invariantes (Lógica de Hoare)
            record EnergyWaterState(double totalSolar, double usedSolar, double availableWaterLiters, double usedWaterLiters) {}

            EnergyWaterState stateInit = new EnergyWaterState(solarGenerationKw, 0.0, waterReserveM3 * 1000.0, 0.0);
            StateInvariant<EnergyWaterState> energyConservation = new StateInvariant<>(
                    "Conservación de Energía",
                    "La potencia despachada no puede superar la generación solar disponible",
                    s -> s.usedSolar() <= s.totalSolar() + 1e-6
            );
            StateInvariant<EnergyWaterState> waterConservation = new StateInvariant<>(
                    "Conservación de Masa de Agua",
                    "El agua consumida no puede superar la reserva disponible",
                    s -> s.usedWaterLiters() <= s.availableWaterLiters() + 1e-6
            );

            HoareTriple<EnergyWaterState> dispatchTriple = new HoareTriple<>(
                    "Contrato de Despacho Agro-Energía-H2",
                    s -> s.totalSolar() >= 0 && s.availableWaterLiters() >= 0,
                    s -> new EnergyWaterState(s.totalSolar(), allocatedH2Kw + allocatedIrrigationKw, s.availableWaterLiters(), totalWaterLitersPerHour),
                    (s0, s1) -> s1.usedSolar() <= s0.totalSolar() && s1.usedWaterLiters() <= s0.availableWaterLiters()
            );

            VerificationCertificate cert = formalVerifier.verifyTriple(
                    dispatchTriple,
                    stateInit,
                    List.of(energyConservation, waterConservation)
            );

            String planId = "PLAN-AGRO-H2-" + UUID.randomUUID().toString().substring(0, 8);

            return new AgroEnergyHydrogenDispatchPlan(
                    planId,
                    solarGenerationKw,
                    allocatedH2Kw,
                    allocatedIrrigationKw,
                    expectedH2KgPerHour,
                    totalWaterLitersPerHour,
                    cert.proofDigest(),
                    cert.verified()
            );
        } finally {
            lock.unlock();
        }
    }
}
