package com.corp.proyectoomniplanetaryhypertwin.domain.port.in;

import com.corp.proyectoomniplanetaryhypertwin.domain.model.HyperPlanetaryTensorNexusNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageHyperPlanetaryTensorNexusNodeUseCase {
    HyperPlanetaryTensorNexusNode createHyperPlanetaryTensorNexusNode(String tenantId, String title, double value);
    Optional<HyperPlanetaryTensorNexusNode> findHyperPlanetaryTensorNexusNodeById(String id, String tenantId);
    HyperPlanetaryTensorNexusNode processOptimization(String id, String tenantId);
}
