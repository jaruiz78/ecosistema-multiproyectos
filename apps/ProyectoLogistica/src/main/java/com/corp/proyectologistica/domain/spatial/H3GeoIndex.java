package com.corp.proyectologistica.domain.spatial;

/**
 * Spatial Index for H3 coordinates. O(1) retrieval guarantees.
 */
public record H3GeoIndex(String h3CellId, double demandSurgeFactor) {}
