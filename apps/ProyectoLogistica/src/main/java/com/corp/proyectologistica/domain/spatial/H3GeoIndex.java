package com.corp.proyectologistica.domain.spatial;
public record H3GeoIndex(String h3CellId, double demandSurgeFactor) {
    public double calculateDynamicPricing(double baseFare) {
        return baseFare * demandSurgeFactor;
    }
}
