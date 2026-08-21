package com.corp.proyectointerplanetaryswarmmesh.application.port.out;

import com.corp.proyectointerplanetaryswarmmesh.domain.BundleProtocolDtnPacket;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface DtnPacketRepositoryPort {
    void save(BundleProtocolDtnPacket packet);
    Optional<BundleProtocolDtnPacket> findById(String bundleId);
}
