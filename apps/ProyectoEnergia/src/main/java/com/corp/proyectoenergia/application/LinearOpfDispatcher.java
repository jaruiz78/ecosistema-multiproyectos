package com.corp.proyectoenergia.application;

import com.corp.proyectoenergia.domain.grid.PowerNode;
import java.util.List;
import java.util.stream.Collectors;

public class LinearOpfDispatcher {
    public List<PowerNode> dispatchPower(List<PowerNode> grid, double additionalLoad) {
        double loadPerNode = additionalLoad / grid.size();
        return grid.stream()
            .map(n -> new PowerNode(n.nodeId(), n.generationCapacity(), n.currentLoad() + loadPerNode))
            .collect(Collectors.toList());
    }
}
