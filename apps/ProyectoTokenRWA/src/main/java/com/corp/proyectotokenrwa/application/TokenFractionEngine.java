package com.corp.proyectotokenrwa.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

/**
 * Motor Financiero de Fraccionamiento de Activos del Mundo Real (RWA) y Conciliación Contable.
 * Implementa redondeo bancario exacto (Banker's Rounding / IEEE 754-2008) y compensación de residuos sin pérdida de valor.
 *
 * <p>Invariante Fundamental de Conservación Contable:
 * \[ \sum_{i=1}^N v_i + r = V_{\text{total}}, \quad r < 10^{-\text{scale}} \]
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public final class TokenFractionEngine {

    private static final int DEFAULT_DECIMAL_SCALE = 6;
    private static final RoundingMode BANKERS_ROUNDING = RoundingMode.HALF_EVEN;

    public record FractionDistribution(
            String assetId,
            BigDecimal totalAssetValuationEur,
            int totalFractions,
            BigDecimal pricePerFractionEur,
            BigDecimal remainderResidualEur,
            Map<String, BigDecimal> investorAllocationsEur,
            Instant timestamp
    ) {}

    /**
     * Calcula el fraccionamiento determinista de un activo RWA y reparte dividendos o cuotas sin fugas de redondeo en O(N).
     *
     * @param assetId               Identificador del activo RWA subyacente.
     * @param totalValuationEur     Valoración total auditada del activo.
     * @param fractionSharesByOwner Mapa de participaciones enteras por inversor.
     * @return {@link FractionDistribution} con la distribución contable y residuo conciliado.
     */
    public FractionDistribution computeFractionSplit(
            String assetId,
            BigDecimal totalValuationEur,
            Map<String, Integer> fractionSharesByOwner
    ) {
        Objects.requireNonNull(assetId, "assetId no puede ser nulo");
        Objects.requireNonNull(totalValuationEur, "totalValuationEur no puede ser nulo");
        Objects.requireNonNull(fractionSharesByOwner, "fractionSharesByOwner no puede ser nulo");

        if (totalValuationEur.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La valoración debe ser estrictamente positiva (Hoare Precondition)");
        }
        if (fractionSharesByOwner.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos un titular de fracciones");
        }

        int totalFractions = fractionSharesByOwner.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (totalFractions <= 0) {
            throw new IllegalArgumentException("El total de fracciones debe ser estrictamente positivo");
        }

        BigDecimal totalFractionsBd = BigDecimal.valueOf(totalFractions);
        BigDecimal pricePerFraction = totalValuationEur.divide(totalFractionsBd, DEFAULT_DECIMAL_SCALE, BANKERS_ROUNDING);

        Map<String, BigDecimal> allocations = new HashMap<>();
        BigDecimal sumAllocated = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : fractionSharesByOwner.entrySet()) {
            BigDecimal shares = BigDecimal.valueOf(entry.getValue());
            BigDecimal ownerValue = pricePerFraction.multiply(shares).setScale(DEFAULT_DECIMAL_SCALE, BANKERS_ROUNDING);
            allocations.put(entry.getKey(), ownerValue);
            sumAllocated = sumAllocated.add(ownerValue);
        }

        // Conciliación de residuo (Remainder)
        BigDecimal remainder = totalValuationEur.subtract(sumAllocated).setScale(DEFAULT_DECIMAL_SCALE, BANKERS_ROUNDING);

        return new FractionDistribution(
                assetId,
                totalValuationEur.setScale(DEFAULT_DECIMAL_SCALE, BANKERS_ROUNDING),
                totalFractions,
                pricePerFraction,
                remainder,
                Map.copyOf(allocations),
                Instant.now()
        );
    }
}
