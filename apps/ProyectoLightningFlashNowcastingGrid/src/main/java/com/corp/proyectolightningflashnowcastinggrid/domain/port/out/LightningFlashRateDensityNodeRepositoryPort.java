package com.corp.proyectolightningflashnowcastinggrid.domain.port.out;

import com.corp.proyectolightningflashnowcastinggrid.domain.model.LightningFlashRateDensityNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LightningFlashRateDensityNodeRepositoryPort {
    LightningFlashRateDensityNode save(LightningFlashRateDensityNode entity);
    Optional<LightningFlashRateDensityNode> findById(String id, String tenantId);
}
