package com.corp.proyectoredparadorestwin.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: RedParadoresTwin.
 * Arquitectura Hexagonal y DDD en Java 25.
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Facultad I - DDD</a>
 */
public record RedParadoresTwin(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {
    public RedParadoresTwin {
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular multi-tenant");
        if (value < 0.0) {
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }
    }

    public RedParadoresTwin withStatus(String newStatus) {
        return new RedParadoresTwin(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }
}
