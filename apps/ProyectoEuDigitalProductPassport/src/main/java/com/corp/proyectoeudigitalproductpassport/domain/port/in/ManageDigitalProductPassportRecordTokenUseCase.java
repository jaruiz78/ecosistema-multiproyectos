package com.corp.proyectoeudigitalproductpassport.domain.port.in;

import com.corp.proyectoeudigitalproductpassport.domain.model.DigitalProductPassportRecordToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDigitalProductPassportRecordTokenUseCase {
    DigitalProductPassportRecordToken createDigitalProductPassportRecordToken(String tenantId, String title, double value);
    Optional<DigitalProductPassportRecordToken> findDigitalProductPassportRecordTokenById(String id, String tenantId);
    DigitalProductPassportRecordToken processOptimization(String id, String tenantId);
}
