package com.corp.proyectointerplanetaryswarmmesh.domain;

import java.io.Serializable;

/**
 * Representa un paquete de red tolerante a retrasos interplanetario (RFC 5050 Bundle Protocol) con custodia.
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
