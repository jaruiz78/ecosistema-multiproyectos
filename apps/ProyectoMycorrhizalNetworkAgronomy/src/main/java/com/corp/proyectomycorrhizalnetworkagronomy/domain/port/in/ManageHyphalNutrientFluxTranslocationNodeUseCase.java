package com.corp.proyectomycorrhizalnetworkagronomy.domain.port.in;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageHyphalNutrientFluxTranslocationNodeUseCase {
    HyphalNutrientFluxTranslocationNode createHyphalNutrientFluxTranslocationNode(String tenantId, String title, double value);
    Optional<HyphalNutrientFluxTranslocationNode> findHyphalNutrientFluxTranslocationNodeById(String id, String tenantId);
    HyphalNutrientFluxTranslocationNode processOptimization(String id, String tenantId);
}
