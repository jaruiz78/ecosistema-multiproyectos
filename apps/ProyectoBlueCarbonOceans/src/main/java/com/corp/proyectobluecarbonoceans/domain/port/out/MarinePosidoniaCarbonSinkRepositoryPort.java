package com.corp.proyectobluecarbonoceans.domain.port.out;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MarinePosidoniaCarbonSinkRepositoryPort {
    MarinePosidoniaCarbonSink save(MarinePosidoniaCarbonSink entity);
    Optional<MarinePosidoniaCarbonSink> findById(String id, String tenantId);
}
