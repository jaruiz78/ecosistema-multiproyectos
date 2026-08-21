package com.corp.proyectospacetrafficcoordination.infrastructure.adapter.out.persistence;

import com.corp.proyectospacetrafficcoordination.domain.model.LeoSatelliteTrack;
import com.corp.proyectospacetrafficcoordination.domain.port.out.SpaceTrackRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySpaceTrackRepositoryAdapter implements SpaceTrackRepositoryPort {

    private final Map<String, LeoSatelliteTrack> tracks = new ConcurrentHashMap<>();

    @Override
    public LeoSatelliteTrack save(LeoSatelliteTrack track) {
        tracks.put(track.noradCatalogId(), track);
        return track;
    }

    @Override
    public Optional<LeoSatelliteTrack> findById(String noradId) {
        return Optional.ofNullable(tracks.get(noradId));
    }

    @Override
    public List<LeoSatelliteTrack> findAll() {
        return new ArrayList<>(tracks.values());
    }
}
