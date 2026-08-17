package com.corp.proyectocircular.application;

import com.corp.proyectocircular.domain.model.BiowasteBatch;

import java.util.Objects;

/**
 * Servicio de Aplicación de Alta Precisión para la Valorización Energética y Agronómica de Biorresiduos.
 * Implementa cinética de Arrhenius para biodigestión anaerobia y balance estequiométrico de biometano.
 *
 * <p>Ecuaciones Gobernantes:
 * 1. Tasa Cinética de Arrhenius:
 *    \( k(T) = A \cdot \exp\left(-\frac{E_a}{R \cdot (T + 273.15)}\right) \)
 * 2. Rendimiento Energético de Metano:
 *    \( E_{\text{kwh}} = V_{\text{biometano}} \times 9.97 \text{ kWh/Nm}^3 \)
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public final class BiowasteValorizerService {

    private static final double UNIVERSAL_GAS_CONSTANT_R = 8.314462; // J/(mol*K)
    private static final double METHANE_ENERGY_DENSITY_KWH_NM3 = 9.97; // kWh/Nm3
    private static final double ARRHENIUS_PRE_EXPONENTIAL_A = 1.25e5; // 1/day
    private static final double ACTIVATION_ENERGY_EA = 48500.0; // J/mol
    private static final double METHANE_CONCENTRATION_RATIO = 0.65; // 65% CH4 en biogás típico

    /**
     * Resultado del cálculo de balance de valorización energética y compostaje.
     */
    public record ValorizationYield(
            String batchId,
            double dryMatterMassKg,
            double theoreticalBiogasVolumeNm3,
            double biomethaneVolumeNm3,
            double totalEnergyYieldKwh,
            double digestateCompostMassKg,
            double kineticRatePerDay,
            double carbonOffsetKgCo2
    ) {}

    /**
     * Calcula el balance estequiométrico y la energía neta generable a partir de un lote de biorresiduos en O(1).
     *
     * @param batch       Lote de biorresiduos con especificación de humedad y masa.
     * @param temperature Temperatura de digestión anaerobia en grados Celsius (rango mesofílico/termofílico típico: 20°C a 60°C).
     * @return {@link ValorizationYield} con los rendimientos calculados.
     */
    public ValorizationYield calculateValorization(BiowasteBatch batch, double temperature) {
        Objects.requireNonNull(batch, "El lote no puede ser nulo");
        if (temperature < -273.15) {
            throw new IllegalArgumentException("La temperatura no puede ser inferior al cero absoluto");
        }

        // 1. Materia Seca (Dry Matter)
        double dryMatterRatio = (100.0 - batch.moisturePercent()) / 100.0;
        double dryMatterMassKg = batch.organicMassKg() * dryMatterRatio;

        // 2. Volumen de Biogás y Biometano
        // BMP está expresado en Nm3 por tonelada de materia fresca
        double tonsOfBiowaste = batch.organicMassKg() / 1000.0;
        double theoreticalBiogasVolumeNm3 = tonsOfBiowaste * batch.biochemicalMethanePotentialNm3PerTon();
        double biomethaneVolumeNm3 = theoreticalBiogasVolumeNm3 * METHANE_CONCENTRATION_RATIO;

        // 3. Rendimiento Energético en kWh
        double totalEnergyYieldKwh = biomethaneVolumeNm3 * METHANE_ENERGY_DENSITY_KWH_NM3;

        // 4. Digestato / Compost Remanente (estimado ~70% de la materia inicial tras desgasificación)
        double digestateCompostMassKg = batch.organicMassKg() * (1.0 - (theoreticalBiogasVolumeNm3 * 1.15 / 1000.0));
        if (digestateCompostMassKg < 0.0) {
            digestateCompostMassKg = dryMatterMassKg * 0.5;
        }

        // 5. Cinética de Arrhenius
        double temperatureKelvin = temperature + 273.15;
        double exponent = -ACTIVATION_ENERGY_EA / (UNIVERSAL_GAS_CONSTANT_R * temperatureKelvin);
        double kineticRatePerDay = ARRHENIUS_PRE_EXPONENTIAL_A * Math.exp(exponent);

        // 6. Huella de Carbono Evitada (Factor aprox: 0.45 kg CO2eq por kWh generado vs mix fósil)
        double carbonOffsetKgCo2 = totalEnergyYieldKwh * 0.45;

        return new ValorizationYield(
                batch.batchId(),
                dryMatterMassKg,
                theoreticalBiogasVolumeNm3,
                biomethaneVolumeNm3,
                totalEnergyYieldKwh,
                digestateCompostMassKg,
                kineticRatePerDay,
                carbonOffsetKgCo2
        );
    }
}
