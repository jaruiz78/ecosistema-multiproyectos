package com.corp.proyectophotonicopticalcompute.domain.port.in;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManagePhotonicInterferometerCoreNodeUseCase {
    PhotonicInterferometerCoreNode createPhotonicInterferometerCoreNode(String tenantId, String title, double value);
    Optional<PhotonicInterferometerCoreNode> findPhotonicInterferometerCoreNodeById(String id, String tenantId);
    PhotonicInterferometerCoreNode processOptimization(String id, String tenantId);
}
