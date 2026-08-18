package com.corp.proyectomicrogravitybiotechlaboratory.domain.port.out;

import com.corp.proyectomicrogravitybiotechlaboratory.domain.model.MicrogravityGProfileAccelerationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MicrogravityGProfileAccelerationNodeRepositoryPort {
    MicrogravityGProfileAccelerationNode save(MicrogravityGProfileAccelerationNode entity);
    Optional<MicrogravityGProfileAccelerationNode> findById(String id, String tenantId);
}
