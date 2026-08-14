package com.proyecto.maritime.domain;

/**
 * Modelo de dominio puro para la asignación de muelles a buques portacontenedores.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
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
