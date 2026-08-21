package com.corp.proyectoatomicforcenanotopography.domain.port.in;

import com.corp.proyectoatomicforcenanotopography.domain.model.AfmCantileverDeflectionScanNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageAfmCantileverDeflectionScanNodeUseCase {
    AfmCantileverDeflectionScanNode createAfmCantileverDeflectionScanNode(String tenantId, String title, double value);
    Optional<AfmCantileverDeflectionScanNode> findAfmCantileverDeflectionScanNodeById(String id, String tenantId);
    AfmCantileverDeflectionScanNode processOptimization(String id, String tenantId);
}
