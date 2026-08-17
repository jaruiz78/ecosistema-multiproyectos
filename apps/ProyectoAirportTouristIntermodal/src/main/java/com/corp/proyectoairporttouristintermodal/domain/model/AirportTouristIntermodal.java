package com.corp.proyectoairporttouristintermodal.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: AirportTouristIntermodal.
 * Arquitectura Hexagonal y DDD en Java 25.
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Facultad I - DDD</a>
 */
public record AirportTouristIntermodal(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {
    public AirportTouristIntermodal {
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular multi-tenant");
        if (value < 0.0) {
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }
    }

    public AirportTouristIntermodal withStatus(String newStatus) {
        return new AirportTouristIntermodal(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }
}
