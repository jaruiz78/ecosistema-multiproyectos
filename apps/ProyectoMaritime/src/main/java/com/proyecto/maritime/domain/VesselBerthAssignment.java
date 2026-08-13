package com.proyecto.maritime.domain;

/**
 * Modelo de dominio puro para la asignación de muelles a buques portacontenedores.
 */
public record VesselBerthAssignment(
        String vesselId,
        String berthId,
        int containerTeuCount,
        long estimatedTurnaroundMinutes,
        boolean allocated
) {
    public VesselBerthAssignment {
        if (vesselId == null || vesselId.isBlank()) {
            throw new IllegalArgumentException("vesselId no puede ser nulo o vacío");
        }
        if (containerTeuCount <= 0) {
            throw new IllegalArgumentException("El número de TEUs debe ser mayor a 0");
        }
    }

    public VesselBerthAssignment withAllocation(String assignedBerthId, long turnaroundMin) {
        return new VesselBerthAssignment(vesselId, assignedBerthId, containerTeuCount, turnaroundMin, true);
    }
}
