package com.corp.proyectophotonicopticalcompute.domain.port.out;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface PhotonicInterferometerCoreNodeRepositoryPort {
    PhotonicInterferometerCoreNode save(PhotonicInterferometerCoreNode entity);
    Optional<PhotonicInterferometerCoreNode> findById(String id, String tenantId);
}
