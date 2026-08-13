package com.proyecto.generalista.application;

import com.proyecto.generalista.domain.EnterpriseTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnterprisePlatformServiceTest {

    @Test
    void testProcessTaskSuccess() {
        EnterprisePlatformService service = new EnterprisePlatformService();
        EnterpriseTask task = new EnterpriseTask("task_001", "tenant_acme", "Generar reporte RAG", false);

        EnterpriseTask processed = service.processTask(task);

        assertNotNull(processed);
        assertTrue(processed.completed());
    }
}
