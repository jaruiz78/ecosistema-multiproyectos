package com.proyecto.generalista.domain;

/**
 * Modelo de dominio puro para una Tarea Empresarial Generalista.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record EnterpriseTask(
        String taskId,
        String tenantId,
        String title,
        boolean completed
) {
    public EnterpriseTask {
        if (taskId == null || taskId.isBlank()) {
            throw new IllegalArgumentException("taskId no puede ser nulo o vacío");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId no puede ser nulo o vacío");
        }
    }

    public EnterpriseTask withStatus(boolean isCompleted) {
        return new EnterpriseTask(taskId, tenantId, title, isCompleted);
    }
}
