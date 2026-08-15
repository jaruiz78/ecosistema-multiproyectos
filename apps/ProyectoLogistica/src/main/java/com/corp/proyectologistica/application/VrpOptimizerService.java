package com.corp.proyectologistica.application;

import com.corp.proyectologistica.domain.vrp.RouteRecord;
import java.util.List;

import com.corp.proyectologistica.domain.spatial.H3GeoIndex;
import java.util.stream.Collectors;
import java.util.Comparator;

public class VrpOptimizerService {
    public List<RouteRecord> optimizeRoutes(List<RouteRecord> routes, H3GeoIndex index) {
        return routes.stream()
            .map(r -> new RouteRecord(r.routeId(), index.h3CellId(), r.priorityScore() * index.demandSurgeFactor(), r.isEscrowSettled()))
            .sorted(Comparator.comparing(RouteRecord::priorityScore).reversed())
            .collect(Collectors.toList());
    }
}
