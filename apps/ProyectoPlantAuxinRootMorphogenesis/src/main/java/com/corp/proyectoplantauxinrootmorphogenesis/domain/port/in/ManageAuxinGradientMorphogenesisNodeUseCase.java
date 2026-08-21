package com.corp.proyectoplantauxinrootmorphogenesis.domain.port.in;

import com.corp.proyectoplantauxinrootmorphogenesis.domain.model.AuxinGradientMorphogenesisNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageAuxinGradientMorphogenesisNodeUseCase {
    AuxinGradientMorphogenesisNode createAuxinGradientMorphogenesisNode(String tenantId, String title, double value);
    Optional<AuxinGradientMorphogenesisNode> findAuxinGradientMorphogenesisNodeById(String id, String tenantId);
    AuxinGradientMorphogenesisNode processOptimization(String id, String tenantId);
}
