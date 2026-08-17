package com.corp.geogrid.h3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test hermético de TDD para el Motor Geoespacial H3 3D.
 * Política Zero-Mockito: Stubs matemáticos puros.
 */
class H3Spatial3DGridEngineTest {

    @Test
    @DisplayName("Creación de celda volumétrica H3 3D y clave de cuantización")
    void testVolumetricCellCreation() {
        var cell = H3Spatial3DGridEngine.createCell("88390cb337fffff", 40.4168, -3.7038, 654.5, 10);
        Assertions.assertEquals("88390cb337fffff:Z650", cell.volumetricKey());
        Assertions.assertEquals(650, cell.altitudeBandMeters());
    }

    @Test
    @DisplayName("Cálculo de vector espacial 3D y pendiente topográfica")
    void testSpatial3DVectorCalculation() {
        var origin = H3Spatial3DGridEngine.createCell("88390cb337fffff", 40.4168, -3.7038, 600.0, 10);
        var dest = H3Spatial3DGridEngine.createCell("88390cb335fffff", 40.4268, -3.7038, 700.0, 10);

        var vector = H3Spatial3DGridEngine.calculate3DVector(origin, dest);
        Assertions.assertTrue(vector.distance2Dkm() > 1.0);
        Assertions.assertEquals(100.0, vector.elevationDeltaMeters(), 0.001);
        Assertions.assertTrue(vector.distance3Dkm() >= vector.distance2Dkm());
        Assertions.assertTrue(vector.slopePercentage() > 0.0);
    }

    @Test
    @DisplayName("Cálculo de energía mecánica de vehículo en pendiente (AppViajes)")
    void testVehicleEnergyCalculation() {
        var origin = H3Spatial3DGridEngine.createCell("H3_A", 40.0, -3.0, 500.0, 10);
        var dest = H3Spatial3DGridEngine.createCell("H3_B", 40.01, -3.0, 600.0, 10);
        var vector = H3Spatial3DGridEngine.calculate3DVector(origin, dest);

        // Vehículo de 1500 kg, coeficiente de fricción 0.015
        double energyJoules = H3Spatial3DGridEngine.calculateVehicleEnergyJoules(1500.0, vector, 0.015);
        Assertions.assertTrue(energyJoules > 0.0);
    }

    @Test
    @DisplayName("Cálculo de pérdida de carga de Darcy-Weisbach (SaaSRegantes)")
    void testHydraulicHeadLoss() {
        // Tubería de 1000m, diámetro 0.3m, velocidad 1.5 m/s, factor fricción f=0.02
        double headLoss = H3Spatial3DGridEngine.calculateHydraulicHeadLossMeters(1000.0, 0.3, 1.5, 0.02);
        Assertions.assertTrue(headLoss > 0.0);
        Assertions.assertEquals(7.647, headLoss, 0.01);
    }
}
