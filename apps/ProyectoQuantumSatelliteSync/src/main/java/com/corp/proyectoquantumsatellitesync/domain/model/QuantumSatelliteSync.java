package com.corp.proyectoquantumsatellitesync.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: QuantumSatelliteSync.
 * Arquitectura Hexagonal y DDD en Java 25.
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Facultad I - DDD</a>
 */
public record QuantumSatelliteSync(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {
    public QuantumSatelliteSync {
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular multi-tenant");
        if (value < 0.0) {
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }
    }

    public QuantumSatelliteSync withStatus(String newStatus) {
        return new QuantumSatelliteSync(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }
}
