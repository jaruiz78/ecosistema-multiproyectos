package com.corp.proyectolightningflashnowcastinggrid.domain.port.in;

import com.corp.proyectolightningflashnowcastinggrid.domain.model.LightningFlashRateDensityNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLightningFlashRateDensityNodeUseCase {
    LightningFlashRateDensityNode createLightningFlashRateDensityNode(String tenantId, String title, double value);
    Optional<LightningFlashRateDensityNode> findLightningFlashRateDensityNodeById(String id, String tenantId);
    LightningFlashRateDensityNode processOptimization(String id, String tenantId);
}
