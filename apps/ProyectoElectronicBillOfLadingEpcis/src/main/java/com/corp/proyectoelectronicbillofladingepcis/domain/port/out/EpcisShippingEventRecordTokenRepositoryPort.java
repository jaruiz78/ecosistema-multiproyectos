package com.corp.proyectoelectronicbillofladingepcis.domain.port.out;

import com.corp.proyectoelectronicbillofladingepcis.domain.model.EpcisShippingEventRecordToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EpcisShippingEventRecordTokenRepositoryPort {
    EpcisShippingEventRecordToken save(EpcisShippingEventRecordToken entity);
    Optional<EpcisShippingEventRecordToken> findById(String id, String tenantId);
}
