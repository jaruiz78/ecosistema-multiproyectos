package com.corp.proyectobluecarbonoceans.domain.port.in;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMarinePosidoniaCarbonSinkUseCase {
    MarinePosidoniaCarbonSink createMarinePosidoniaCarbonSink(String tenantId, String title, double value);
    Optional<MarinePosidoniaCarbonSink> findMarinePosidoniaCarbonSinkById(String id, String tenantId);
    MarinePosidoniaCarbonSink processOptimization(String id, String tenantId);
}
