package com.corp.proyectophagedisplaydirectedevolution.domain.port.in;

import com.corp.proyectophagedisplaydirectedevolution.domain.model.PhageDisplayAffinityEnrichmentToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManagePhageDisplayAffinityEnrichmentTokenUseCase {
    PhageDisplayAffinityEnrichmentToken createPhageDisplayAffinityEnrichmentToken(String tenantId, String title, double value);
    Optional<PhageDisplayAffinityEnrichmentToken> findPhageDisplayAffinityEnrichmentTokenById(String id, String tenantId);
    PhageDisplayAffinityEnrichmentToken processOptimization(String id, String tenantId);
}
