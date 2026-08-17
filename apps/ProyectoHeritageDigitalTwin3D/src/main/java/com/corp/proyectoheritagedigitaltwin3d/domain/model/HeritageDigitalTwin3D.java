package com.corp.proyectoheritagedigitaltwin3d.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: HeritageDigitalTwin3D.
 * Arquitectura Hexagonal y DDD en Java 25.
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Facultad I - DDD</a>
 */
public record HeritageDigitalTwin3D(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {
    public HeritageDigitalTwin3D {
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular multi-tenant");
        if (value < 0.0) {
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }
    }

    public HeritageDigitalTwin3D withStatus(String newStatus) {
        return new HeritageDigitalTwin3D(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }
}
