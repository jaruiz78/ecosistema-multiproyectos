package com.proyecto.generalista.domain;

/**
 * Modelo de dominio puro para una Tarea Empresarial Generalista.
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
