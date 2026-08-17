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
    public static double calculateHydraulicHeadLossMeters(double lengthMeters, double diameterMeters, double flowVelocityMps, double frictionFactor) {
        if (lengthMeters <= 0 || diameterMeters <= 0 || flowVelocityMps < 0 || frictionFactor <= 0) {
            throw new IllegalArgumentException("Parámetros hidráulicos inválidos");
        }
        return frictionFactor * (lengthMeters / diameterMeters) * ((flowVelocityMps * flowVelocityMps) / (2 * GRAVITY));
    }
}
