package com.corp.proyectocircular.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Agregado de Dominio para Lotes de Biorresiduos y Economía Circular.
 * Modela la composición de masa orgánica, humedad y potencial biometanogénico.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public record BiowasteBatch(
        String batchId,
        String tenantId,
        double organicMassKg,
        double moisturePercent,
        double biochemicalMethanePotentialNm3PerTon,
        double carbonNitrogenRatio,
        Instant timestamp
) {
    public BiowasteBatch {
        Objects.requireNonNull(batchId, "El batchId es obligatorio");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio");
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (organicMassKg <= 0.0) {
            throw new IllegalArgumentException("La masa orgánica debe ser estrictamente positiva (Hoare Precondition)");
        }
        if (moisturePercent < 0.0 || moisturePercent > 100.0) {
            throw new IllegalArgumentException("El porcentaje de humedad debe estar comprendido entre 0.0% y 100.0%");
        }
        if (biochemicalMethanePotentialNm3PerTon < 0.0) {
            throw new IllegalArgumentException("El potencial biometanogénico no puede ser negativo");
        }
        if (carbonNitrogenRatio <= 0.0) {
            throw new IllegalArgumentException("La relación C/N debe ser estrictamente positiva");
        }
    }
}
