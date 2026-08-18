package com.corp.proyectoelectronicbillofladingepcis.domain.port.in;

import com.corp.proyectoelectronicbillofladingepcis.domain.model.EpcisShippingEventRecordToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEpcisShippingEventRecordTokenUseCase {
    EpcisShippingEventRecordToken createEpcisShippingEventRecordToken(String tenantId, String title, double value);
    Optional<EpcisShippingEventRecordToken> findEpcisShippingEventRecordTokenById(String id, String tenantId);
    EpcisShippingEventRecordToken processOptimization(String id, String tenantId);
}
