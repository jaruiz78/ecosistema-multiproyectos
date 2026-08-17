package com.corp.proyectointerplanetaryswarmmesh.application.port.out;

import com.corp.proyectointerplanetaryswarmmesh.domain.BundleProtocolDtnPacket;
import java.util.Optional;

public interface DtnPacketRepositoryPort {
    void save(BundleProtocolDtnPacket packet);
    Optional<BundleProtocolDtnPacket> findById(String bundleId);
}
