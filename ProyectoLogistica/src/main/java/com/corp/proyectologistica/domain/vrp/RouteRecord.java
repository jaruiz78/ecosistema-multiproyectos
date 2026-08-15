package com.corp.proyectologistica.domain.vrp;

/**
 * Domain record for Stochastic VRP (Vehicle Routing Problem).
 * Pure Java 25, Zero-Mockito.
 */
public record RouteRecord(String routeId, String h3GeoIndex, double priorityScore, boolean isEscrowSettled) {}
