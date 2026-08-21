package com.corp.proyectointerplanetaryswarmmesh.domain;

import java.io.Serializable;

/**
 * Representa un paquete de red tolerante a retrasos interplanetario (RFC 5050 Bundle Protocol) con custodia.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record BundleProtocolDtnPacket(
        String bundleId,
        String sourceNodeEid,
        String destinationNodeEid,
        long timeToLiveSeconds,
        boolean custodyTransferRequested,
        int payloadSizeKb
) implements Serializable {

    public static BundleProtocolDtnPacket create(String id, String src, String dst, long ttl, int sizeKb) {
        return new BundleProtocolDtnPacket(id, src, dst, ttl, true, sizeKb);
    }
}
