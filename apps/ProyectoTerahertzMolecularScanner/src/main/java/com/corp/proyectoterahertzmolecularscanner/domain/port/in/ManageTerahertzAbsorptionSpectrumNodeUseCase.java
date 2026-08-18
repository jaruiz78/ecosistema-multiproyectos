package com.corp.proyectoterahertzmolecularscanner.domain.port.in;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageTerahertzAbsorptionSpectrumNodeUseCase {
    TerahertzAbsorptionSpectrumNode createTerahertzAbsorptionSpectrumNode(String tenantId, String title, double value);
    Optional<TerahertzAbsorptionSpectrumNode> findTerahertzAbsorptionSpectrumNodeById(String id, String tenantId);
    TerahertzAbsorptionSpectrumNode processOptimization(String id, String tenantId);
}
