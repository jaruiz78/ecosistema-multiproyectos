package com.corp.proyectoterahertzmolecularscanner.domain.port.out;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface TerahertzAbsorptionSpectrumNodeRepositoryPort {
    TerahertzAbsorptionSpectrumNode save(TerahertzAbsorptionSpectrumNode entity);
    Optional<TerahertzAbsorptionSpectrumNode> findById(String id, String tenantId);
}
