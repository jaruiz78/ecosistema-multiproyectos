package com.corp.proyectologistica.domain.vrp;
public record RouteRecord(String routeId, String h3GeoIndex, double priorityScore, boolean isEscrowSettled) {
    public RouteRecord withEscrowSettled(boolean settled) {
        return new RouteRecord(routeId, h3GeoIndex, priorityScore, settled);
    }
}
