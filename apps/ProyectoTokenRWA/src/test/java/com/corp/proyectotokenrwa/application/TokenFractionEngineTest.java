package com.corp.proyectotokenrwa.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link TokenFractionEngine}.
 */
class TokenFractionEngineTest {

    private final TokenFractionEngine engine = new TokenFractionEngine();

    @Test
    @DisplayName("Debe fraccionar activos con precisión de 6 decimales y conciliar residuos contables")
    void shouldComputeFractionSplitWithoutLeakage() {
        BigDecimal valuation = new BigDecimal("1000000.00"); // 1M EUR
        Map<String, Integer> owners = Map.of(
                "INVESTOR_A", 333333,
                "INVESTOR_B", 333333,
                "INVESTOR_C", 333334
        );

        TokenFractionEngine.FractionDistribution dist = engine.computeFractionSplit(
                "RWA_SOLAR_FARM_01", valuation, owners
        );

        assertNotNull(dist);
        assertEquals(1000000, dist.totalFractions());
        assertEquals(new BigDecimal("1.000000"), dist.pricePerFractionEur());
        assertEquals(new BigDecimal("333333.000000"), dist.investorAllocationsEur().get("INVESTOR_A"));
        assertEquals(new BigDecimal("333334.000000"), dist.investorAllocationsEur().get("INVESTOR_C"));
        assertEquals(new BigDecimal("0.000000"), dist.remainderResidualEur());
    }
}
