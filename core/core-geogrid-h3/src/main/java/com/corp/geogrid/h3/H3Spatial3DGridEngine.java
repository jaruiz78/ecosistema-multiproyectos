package com.corp.geogrid.h3;

import java.util.Objects;

/**
 * Motor de Indexación Geoespacial H3 Volumétrica 3D y Física Topográfica.
 * Modela prismas hexagonales combinando indexación H3 2D con cotas de altitud (Z-Axis),
 * permitiendo calcular pérdidas de carga hidráulicas (SaaSRegantes) y gasto energético en pendientes (AppViajes).
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_ingenieria_geoespacial_h3_osrm">Facultad IX: Geoespacial H3</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 */
public class H3Spatial3DGridEngine {

    public static final double GRAVITY = 9.80665; // m/s^2
    public static final double EARTH_RADIUS_KM = 6371.0;

    public record H3VolumetricCell(
        String h3Index2D,
        int altitudeBandMeters, // Cuantización en bandas (ej. cada 10 metros)
        double baseLat,
        double baseLon,
        double elevationMeters
    ) {
        public H3VolumetricCell {
            Objects.requireNonNull(h3Index2D, "Invariante de Hoare: h3Index2D no puede ser nulo");
            if (altitudeBandMeters < -500 || altitudeBandMeters > 9000) {
                throw new IllegalArgumentException("Invariante de Hoare: Altitud fuera de rango terrestre (-500m a 9000m)");
            }
        }

        public String volumetricKey() {
            return h3Index2D + ":Z" + altitudeBandMeters;
        }
    }

    public record Spatial3DVector(
        double distance2Dkm,
        double elevationDeltaMeters,
        double distance3Dkm,
        double slopePercentage
    ) {}

    public static H3VolumetricCell createCell(String h3Index, double lat, double lon, double elevationMeters, int bandStepMeters) {
        Objects.requireNonNull(h3Index, "h3Index es obligatorio");
        int band = ((int) Math.floor(elevationMeters / bandStepMeters)) * bandStepMeters;
        return new H3VolumetricCell(h3Index, band, lat, lon, elevationMeters);
    }

    public static Spatial3DVector calculate3DVector(H3VolumetricCell origin, H3VolumetricCell dest) {
        Objects.requireNonNull(origin, "Origen no puede ser nulo");
        Objects.requireNonNull(dest, "Destino no puede ser nulo");

        double lat1 = Math.toRadians(origin.baseLat());
        double lon1 = Math.toRadians(origin.baseLon());
        double lat2 = Math.toRadians(dest.baseLat());
        double lon2 = Math.toRadians(dest.baseLon());

        // Fórmula de Haversine para distancia 2D en superficie
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist2Dkm = EARTH_RADIUS_KM * c;

        double deltaElevation = dest.elevationMeters() - origin.elevationMeters();
        double deltaElevationKm = deltaElevation / 1000.0;

        double dist3Dkm = Math.sqrt(dist2Dkm * dist2Dkm + deltaElevationKm * deltaElevationKm);
        double slope = dist2Dkm > 0 ? (deltaElevation / (dist2Dkm * 1000.0)) * 100.0 : 0.0;

        return new Spatial3DVector(dist2Dkm, deltaElevation, dist3Dkm, slope);
    }

    /**
     * Calcula la energía mecánica neta requerida por un vehículo de masa M en una pendiente.
     * E = m * g * deltaH + F_fric * distance (Joules)
     */
    public static double calculateVehicleEnergyJoules(double vehicleMassKg, Spatial3DVector vector, double rollingFrictionCoeff) {
        if (vehicleMassKg <= 0) {
            throw new IllegalArgumentException("La masa del vehículo debe ser positiva");
        }
        double potentialEnergy = vehicleMassKg * GRAVITY * vector.elevationDeltaMeters();
        double distanceMeters = vector.distance3Dkm() * 1000.0;
        double frictionWork = rollingFrictionCoeff * vehicleMassKg * GRAVITY * distanceMeters;
        return Math.max(0.0, potentialEnergy + frictionWork);
    }

    /**
     * Calcula la pérdida de carga hidráulica en tubería presurizada mediante Darcy-Weisbach.
     * h_f = f * (L / D) * (v^2 / 2g) (metros de columna de agua)
     */
    // =========================================================================
    // GEODESIA ELIPSOIDAL WGS-84 (ALGORITMO DE VINCENTY)
    // =========================================================================

    public static final double WGS84_SEMI_MAJOR_AXIS_A = 6378137.0; // Metros
    public static final double WGS84_FLATTENING_F = 1.0 / 298.257223563;
    public static final double WGS84_SEMI_MINOR_AXIS_B = WGS84_SEMI_MAJOR_AXIS_A * (1.0 - WGS84_FLATTENING_F);

    /**
     * Calcula la distancia geodésica elipsoidal WGS-84 precisa en metros entre dos coordenadas.
     * Implementa el algoritmo iterativo de Vincenty con convergencia de 1e-12.
     */
    public static double calculateVincentyDistanceMeters(double lat1Deg, double lon1Deg, double lat2Deg, double lon2Deg) {
        if (lat1Deg == lat2Deg && lon1Deg == lon2Deg) {
            return 0.0;
        }

        double L = Math.toRadians(lon2Deg - lon1Deg);
        double U1 = Math.atan((1.0 - WGS84_FLATTENING_F) * Math.tan(Math.toRadians(lat1Deg)));
        double U2 = Math.atan((1.0 - WGS84_FLATTENING_F) * Math.tan(Math.toRadians(lat2Deg)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double lambda = L;
        double lambdaP;
        double sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        int iterLimit = 100;

        do {
            double sinLambda = Math.sin(lambda);
            double cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) +
                    (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) return 0.0; // Puntos coincidentes

            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SigmaM = (cosSqAlpha != 0.0) ? (cosSigma - 2.0 * sinU1 * sinU2 / cosSqAlpha) : 0.0;

            double C = WGS84_FLATTENING_F / 16.0 * cosSqAlpha * (4.0 + WGS84_FLATTENING_F * (4.0 - 3.0 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1.0 - C) * WGS84_FLATTENING_F * sinAlpha *
                    (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1.0 + 2.0 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0) {
            // Fallback a Haversine esférica si no converge (puntos casi antípodas)
            return calculateHaversineDistanceKm(lat1Deg, lon1Deg, lat2Deg, lon2Deg) * 1000.0;
        }

        double uSq = cosSqAlpha * (WGS84_SEMI_MAJOR_AXIS_A * WGS84_SEMI_MAJOR_AXIS_A - WGS84_SEMI_MINOR_AXIS_B * WGS84_SEMI_MINOR_AXIS_B) /
                (WGS84_SEMI_MINOR_AXIS_B * WGS84_SEMI_MINOR_AXIS_B);
        double A = 1.0 + uSq / 16384.0 * (4096.0 + uSq * (-768.0 + uSq * (320.0 - 175.0 * uSq)));
        double B = uSq / 1024.0 * (256.0 + uSq * (-128.0 + uSq * (74.0 - 47.0 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4.0 * (cosSigma * (-1.0 + 2.0 * cos2SigmaM * cos2SigmaM) -
                B / 6.0 * cos2SigmaM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SigmaM * cos2SigmaM)));

        return WGS84_SEMI_MINOR_AXIS_B * A * (sigma - deltaSigma);
    }

    private static double calculateHaversineDistanceKm(double lat1Deg, double lon1Deg, double lat2Deg, double lon2Deg) {
        double lat1 = Math.toRadians(lat1Deg);
        double lon1 = Math.toRadians(lon1Deg);
        double lat2 = Math.toRadians(lat2Deg);
        double lon2 = Math.toRadians(lon2Deg);
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public static double calculateHydraulicHeadLossMeters(double lengthMeters, double diameterMeters, double flowVelocityMps, double frictionFactor) {
        if (lengthMeters <= 0 || diameterMeters <= 0 || flowVelocityMps < 0 || frictionFactor <= 0) {
            throw new IllegalArgumentException("Parámetros hidráulicos inválidos");
        }
        return frictionFactor * (lengthMeters / diameterMeters) * ((flowVelocityMps * flowVelocityMps) / (2 * GRAVITY));
    }

    /**
     * Calcula en lote continuo de alto rendimiento las distancias 2D de N pares de coordenadas.
     * Diseñado con acceso secuencial directo a memoria para permitir auto-vectorización SIMD del JIT (AVX2/AVX-512).
     */
    public static void calculateBatchHaversineDistancesKm(
            double[] lat1Arr, double[] lon1Arr,
            double[] lat2Arr, double[] lon2Arr,
            double[] outputKm, int count) {
        
        Objects.requireNonNull(lat1Arr, "lat1Arr no puede ser nulo");
        Objects.requireNonNull(outputKm, "outputKm no puede ser nulo");

        final double deg2rad = Math.PI / 180.0;
        final double radius = EARTH_RADIUS_KM;

        for (int i = 0; i < count; i++) {
            double lat1 = lat1Arr[i] * deg2rad;
            double lon1 = lon1Arr[i] * deg2rad;
            double lat2 = lat2Arr[i] * deg2rad;
            double lon2 = lon2Arr[i] * deg2rad;

            double dLat = (lat2 - lat1) * 0.5;
            double dLon = (lon2 - lon1) * 0.5;

            double sinDLat = Math.sin(dLat);
            double sinDLon = Math.sin(dLon);

            double a = sinDLat * sinDLat + Math.cos(lat1) * Math.cos(lat2) * sinDLon * sinDLon;
            double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
            outputKm[i] = radius * c;
        }
    }
}
