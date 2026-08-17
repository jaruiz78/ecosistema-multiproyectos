package com.corp.proyectointerplanetaryswarmmesh.infrastructure.adapter;

import com.corp.proyectointerplanetaryswarmmesh.application.port.out.DtnPacketRepositoryPort;
import com.corp.proyectointerplanetaryswarmmesh.domain.BundleProtocolDtnPacket;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDtnPacketRepositoryAdapter implements DtnPacketRepositoryPort {

    private final Map<String, BundleProtocolDtnPacket> store = new ConcurrentHashMap<>();

    @Override
    public void save(BundleProtocolDtnPacket packet) {
        store.put(packet.bundleId(), packet);
    }

    @Override
    public Optional<BundleProtocolDtnPacket> findById(String bundleId) {
        return Optional.ofNullable(store.get(bundleId));
    }
}
