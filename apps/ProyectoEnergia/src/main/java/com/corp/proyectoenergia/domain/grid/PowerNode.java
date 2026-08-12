package com.corp.proyectoenergia.domain.grid;
public record PowerNode(String nodeId, double generationCapacity, double currentLoad) {
    public double calculateReserve() {
        return generationCapacity - currentLoad;
    }
}
