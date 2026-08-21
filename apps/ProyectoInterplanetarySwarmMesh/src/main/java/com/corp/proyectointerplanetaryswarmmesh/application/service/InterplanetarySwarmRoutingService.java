package com.corp.proyectointerplanetaryswarmmesh.application.service;

import com.corp.proyectointerplanetaryswarmmesh.application.port.out.DtnPacketRepositoryPort;
import com.corp.proyectointerplanetaryswarmmesh.domain.BundleProtocolDtnPacket;
import org.springframework.stereotype.Service;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InterplanetarySwarmRoutingService {

    private final DtnPacketRepositoryPort repositoryPort;

    public InterplanetarySwarmRoutingService(DtnPacketRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public BundleProtocolDtnPacket dispatchBundle(String bundleId, String srcEid, String dstEid, long ttlSeconds, int sizeKb) {
        var packet = BundleProtocolDtnPacket.create(bundleId, srcEid, dstEid, ttlSeconds, sizeKb);
        repositoryPort.save(packet);
        return packet;
    }
}
