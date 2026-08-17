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

    @Test
    @DisplayName("Cálculo de distancia elipsoidal precisa WGS-84 con algoritmo de Vincenty")
    void testVincentyDistanceCalculation() {
        // Madrid Puerta del Sol (40.4168, -3.7038) a Barcelona Plaça Catalunya (41.3870, 2.1700)
        double distanceMeters = H3Spatial3DGridEngine.calculateVincentyDistanceMeters(40.4168, -3.7038, 41.3870, 2.1700);
        
        // Distancia geodésica WGS-84 conocida aprox ~504.6 km = 504600 m
        Assertions.assertTrue(distanceMeters > 500000.0 && distanceMeters < 510000.0);
        Assertions.assertEquals(504646.0, distanceMeters, 2000.0);
        
        // Puntos coincidentes = 0.0
        Assertions.assertEquals(0.0, H3Spatial3DGridEngine.calculateVincentyDistanceMeters(40.0, 3.0, 40.0, 3.0));
    }

    @Test
    @DisplayName("Validación de casos límite en pérdidas hidráulicas de Darcy-Weisbach")
    void testHydraulicHeadLossEdgeCases() {
        // Velocidad de flujo cero produce cero pérdida de carga
        double zeroLoss = H3Spatial3DGridEngine.calculateHydraulicHeadLossMeters(500.0, 0.2, 0.0, 0.02);
        Assertions.assertEquals(0.0, zeroLoss);

        // Diámetro no positivo debe lanzar IllegalArgumentException
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                H3Spatial3DGridEngine.calculateHydraulicHeadLossMeters(500.0, 0.0, 1.5, 0.02)
        );
    }

    @Test
    @DisplayName("Validación de invariantes de Hoare en celdas volumétricas H3")
    void testVolumetricCellInvariants() {
        // Altitud por encima de la estratosfera (>9000m) debe lanzar excepción
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                H3Spatial3DGridEngine.createCell("88390cb337fffff", 0.0, 0.0, 12000.0, 10)
        );

        // Altitud por debajo del lecho marino profundo (<-500m) debe lanzar excepción
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                H3Spatial3DGridEngine.createCell("88390cb337fffff", 0.0, 0.0, -800.0, 10)
        );
    }

    @Test
    @DisplayName("Cálculo en lote de alta velocidad (100.000 operaciones) y precisión con Haversine")
    void testBatchHaversineCalculationAndThroughput() {
        int count = 100_000;
        double[] lat1 = new double[count];
        double[] lon1 = new double[count];
        double[] lat2 = new double[count];
        double[] lon2 = new double[count];
        double[] output = new double[count];

        for (int i = 0; i < count; i++) {
            lat1[i] = 40.4168 + (i * 0.00001);
            lon1[i] = -3.7038;
            lat2[i] = 41.3870;
            lon2[i] = 2.1700 + (i * 0.00001);
        }

        long start = System.nanoTime();
        H3Spatial3DGridEngine.calculateBatchHaversineDistancesKm(lat1, lon1, lat2, lon2, output, count);
        long elapsedNs = System.nanoTime() - start;

        double elapsedMs = elapsedNs / 1_000_000.0;
        double opsPerSec = (count / (double) elapsedNs) * 1_000_000_000.0;

        System.out.printf("[H3 BATCH BENCHMARK] 100,000 distances computed in %.2f ms (%.2f ops/sec, %.2f ns/op)%n",
                elapsedMs, opsPerSec, elapsedNs / (double) count);

        // Validación de exactitud
        Assertions.assertTrue(output[0] > 500.0 && output[0] < 510.0);
        Assertions.assertTrue(output[count - 1] > 500.0 && output[count - 1] < 600.0);
        Assertions.assertTrue(elapsedMs < 100.0, "El cálculo en lote debe completarse en menos de 100ms");
    }
}
