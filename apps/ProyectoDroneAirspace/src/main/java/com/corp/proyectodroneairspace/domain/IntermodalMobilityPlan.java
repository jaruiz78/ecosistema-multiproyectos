package com.corp.proyectodroneairspace.domain;

import java.util.Objects;

/**
 * Plan de enrutamiento y despacho intermodal Aéreo-Terrestre (Drones U-Space + Transfers H3).
 *
 * @param planId              Identificador único del plan intermodal
 * @param originH3Index       Hexágono H3 de origen 2D
 * @param destinationH3Index  Hexágono H3 de destino 2D
 * @param flightAltitudeBandMeters Banda de altitud asignada para el tramo aéreo
 * @param totalDistance3Dkm   Distancia total tridimensional estimada
 * @param droneEstimatedMinutes Tiempo de vuelo estimado del dron en minutos
 * @param groundEstimatedMinutes Tiempo estimado del vehículo terrestre en minutos
 * @param natsEventPublished  Indica si el evento fue propagado por NATS Mesh
 *
 * @see docs/formacion_ecosistema/modulo_8_ingenieria_geoespacial_h3_osrm/01_indexacion_h3_y_routing.md
 */
public record IntermodalMobilityPlan(
        String planId,
        String originH3Index,
        String destinationH3Index,
        int flightAltitudeBandMeters,
        double totalDistance3Dkm,
        double droneEstimatedMinutes,
        double groundEstimatedMinutes,
        boolean natsEventPublished
) {
    public IntermodalMobilityPlan {
        Objects.requireNonNull(planId, "planId no puede ser nulo");
        Objects.requireNonNull(originH3Index, "originH3Index no puede ser nulo");
        Objects.requireNonNull(destinationH3Index, "destinationH3Index no puede ser nulo");
        if (totalDistance3Dkm < 0 || droneEstimatedMinutes < 0 || groundEstimatedMinutes < 0) {
            throw new IllegalArgumentException("Métricas de distancia y tiempo no pueden ser negativas");
        }
    }
}
