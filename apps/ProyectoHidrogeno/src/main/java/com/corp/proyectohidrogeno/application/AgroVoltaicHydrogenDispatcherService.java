package com.corp.proyectohidrogeno.application;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Despacho de Hidrógeno Verde Acoplado a Nexo Agro-Voltaico.
 *
 * <p>Modela la electrólisis PEM (\(P_{\text{específica}} \approx 50\text{ kWh/kg } H_2\))
 * y el consumo estequiométrico de agua desalinizada/depurada (\(9\text{ L } H_2O / \text{kg } H_2\))
 * en \(O(1)\) sin Carrier Thread Pinning.
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 */
@Service
public class AgroVoltaicHydrogenDispatcherService {

    private final ReentrantLock lock = new ReentrantLock();

    // Poder calorífico superior y consumo energético típico de electrolizador PEM
    public static final double KWH_PER_KG_HYDROGEN = 50.0;
    public static final double LITERS_WATER_PER_KG_HYDROGEN = 9.0;

    public record HydrogenDispatchPlan(
            String batchId,
            double hydrogenProducedKg,
            double waterConsumedLiters,
            double surplusPowerUtilizedKwh,
            double efficiencyRatio
    ) {}

    public HydrogenDispatchPlan computeDispatch(
            HydrogenProductionBatch batch,
            double surplusSolarPowerKwh,
            double pemStackEfficiency
    ) {
        Objects.requireNonNull(batch, "batch no puede ser nulo");
        if (surplusSolarPowerKwh < 0.0) throw new IllegalArgumentException("La potencia solar no puede ser negativa");
        if (pemStackEfficiency <= 0.0 || pemStackEfficiency > 1.0) {
            throw new IllegalArgumentException("La eficiencia debe estar en (0.0, 1.0]");
        }

        lock.lock();
        try {
            double effectiveEnergy = surplusSolarPowerKwh * pemStackEfficiency;
            double hydrogenKg = effectiveEnergy / KWH_PER_KG_HYDROGEN;
            double waterLiters = hydrogenKg * LITERS_WATER_PER_KG_HYDROGEN;

            return new HydrogenDispatchPlan(
                    batch.id(),
                    hydrogenKg,
                    waterLiters,
                    surplusSolarPowerKwh,
                    pemStackEfficiency
            );
        } finally {
            lock.unlock();
        }
    }
}
