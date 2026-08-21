package com.corp.proyectospacetrafficcoordination.domain.port.out;

import com.corp.proyectospacetrafficcoordination.domain.model.LeoSatelliteTrack;
import java.util.List;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface SpaceTrackRepositoryPort {
    LeoSatelliteTrack save(LeoSatelliteTrack track);
    Optional<LeoSatelliteTrack> findById(String noradId);
    List<LeoSatelliteTrack> findAll();
}
