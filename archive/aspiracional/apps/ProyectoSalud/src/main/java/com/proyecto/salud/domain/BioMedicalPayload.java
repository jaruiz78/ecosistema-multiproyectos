package com.proyecto.salud.domain;

/**
 * Modelo de dominio puro (Zero-PII HIPAA Compliant) para transporte biomédico crítico.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record BioMedicalPayload(
        String payloadId,
        String specimenType,
        double currentTemperatureCelsius,
        double minTempThreshold,
        double maxTempThreshold,
        boolean coldChainIntact
) {
    public BioMedicalPayload {
        if (payloadId == null || payloadId.isBlank()) {
            throw new IllegalArgumentException("payloadId no puede ser nulo o vacío");
        }
    }

    public BioMedicalPayload withTemperature(double newTempCelsius) {
        boolean intact = newTempCelsius >= minTempThreshold && newTempCelsius <= maxTempThreshold;
        return new BioMedicalPayload(payloadId, specimenType, newTempCelsius, minTempThreshold, maxTempThreshold, intact);
    }
}
