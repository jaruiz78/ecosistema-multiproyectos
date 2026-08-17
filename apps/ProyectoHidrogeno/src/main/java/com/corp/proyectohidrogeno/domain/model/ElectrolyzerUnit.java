package com.corp.proyectohidrogeno.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: ElectrolyzerUnit.
 * 
 * Invariantes de Negocio y Reglas de Dominio:
 * 1. Identificador inmutable no nulo ni vacío.
 * 2. Validación en constructor compacto garantizando consistencia O(1).
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema - Facultad I</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001</a>
 */
public record ElectrolyzerUnit(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {
    public ElectrolyzerUnit {
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular");
        if (value < 0.0) {
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }
    }

    public ElectrolyzerUnit withStatus(String newStatus) {
        return new ElectrolyzerUnit(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }
}
