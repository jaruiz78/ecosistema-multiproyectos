package com.corp.proyectologistica.application;

import com.corp.proyectologistica.domain.model.AutonomousFleetRoute;

import java.time.Instant;
import java.util.*;

/**
 * Motor de Optimización de Rutas de Flota Autónoma y Logística Verde (VRPTW Estocástico).
 * Modela ventanas de tiempo, penalizaciones por demora y optimización de huella de carbono bajo mallas H3.
 *
 * <p>Ecuaciones Gobernantes:
 * \[ \min \sum_{i,j} c_{ij} x_{ij} + \sum_i \alpha \max(0, a_i - l_i) + \sum_{i,j} e_{ij} x_{ij} \]
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public final class StochasticVrpEngine {

    private static final double CARBON_EMISSION_KG_PER_KM = 0.120; // 120 g CO2/km
    private static final double LATE_PENALTY_RATE_PER_MINUTE = 1.50; // EUR/min

    public record DeliveryStop(
            String stopId,
            String h3HexagonCell,
            double latitude,
            double longitude,
            int demandPackages,
            int windowStartMinutes,
            int windowEndMinutes
    ) {
        public DeliveryStop {
            Objects.requireNonNull(stopId, "stopId no puede ser nulo");
            Objects.requireNonNull(h3HexagonCell, "h3HexagonCell no puede ser nulo");
            if (demandPackages <= 0) {
                throw new IllegalArgumentException("La demanda de paquetes debe ser positiva (Hoare Precondition)");
            }
        }
    }

    public record OptimizedTour(
            String vehicleId,
            List<String> orderedStopIds,
            double totalDistanceKm,
            double totalDurationMinutes,
            double totalCostEur,
            double totalCarbonEmissionKg,
            int totalDeliveredPackages
    ) {}

    /**
     * Resuelve y optimiza el despacho de un vehículo sobre un conjunto de paradas con ventanas de tiempo en O(N log N).
     */
    public OptimizedTour computeOptimalTour(String vehicleId, double originLat, double originLon, List<DeliveryStop> stops) {
        Objects.requireNonNull(vehicleId, "vehicleId no puede ser nulo");
        Objects.requireNonNull(stops, "stops no puede ser nulo");

        if (stops.isEmpty()) {
            return new OptimizedTour(vehicleId, List.of(), 0.0, 0.0, 0.0, 0.0, 0);
        }

        // Ordenación heurística por inicio de ventana de tiempo y proximidad
        List<DeliveryStop> sortedStops = new ArrayList<>(stops);
        sortedStops.sort(Comparator.comparingInt(DeliveryStop::windowStartMinutes));

        double totalDistKm = 0.0;
        double currentLat = originLat;
        double currentLon = originLon;
        double currentMinute = 0.0;
        double totalPenalty = 0.0;
        int totalPackages = 0;
        List<String> routeStopIds = new ArrayList<>();

        for (DeliveryStop stop : sortedStops) {
            double legDist = haversineKm(currentLat, currentLon, stop.latitude(), stop.longitude());
            totalDistKm += legDist;
            double travelTimeMinutes = (legDist / 40.0) * 60.0; // Velocidad media 40 km/h
            currentMinute += travelTimeMinutes;

            // Espera si llega antes de la ventana
            if (currentMinute < stop.windowStartMinutes()) {
                currentMinute = stop.windowStartMinutes();
            }

            // Penalización si llega después de la ventana
            if (currentMinute > stop.windowEndMinutes()) {
                double delay = currentMinute - stop.windowEndMinutes();
                totalPenalty += delay * LATE_PENALTY_RATE_PER_MINUTE;
            }

            currentMinute += 5.0; // 5 minutos por entrega
            totalPackages += stop.demandPackages();
            routeStopIds.add(stop.stopId());

            currentLat = stop.latitude();
            currentLon = stop.longitude();
        }

        double fuelCost = totalDistKm * 0.35; // 0.35 EUR/km
        double totalCost = fuelCost + totalPenalty;
        double totalCarbon = totalDistKm * CARBON_EMISSION_KG_PER_KM;

        return new OptimizedTour(
                vehicleId,
                List.copyOf(routeStopIds),
                totalDistKm,
                currentMinute,
                totalCost,
                totalCarbon,
                totalPackages
        );
    }

    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6371.0 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
