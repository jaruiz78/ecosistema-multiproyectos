package com.corp.proyectogreenhydrogendesal.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: GreenHydrogenDesal.
 * Arquitectura Hexagonal y DDD en Java 25.
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Facultad I - DDD</a>
 */
public record GreenHydrogenDesal(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {
    public GreenHydrogenDesal {
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular multi-tenant");
        if (value < 0.0) {
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }
    }

    public GreenHydrogenDesal withStatus(String newStatus) {
        return new GreenHydrogenDesal(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }
}
