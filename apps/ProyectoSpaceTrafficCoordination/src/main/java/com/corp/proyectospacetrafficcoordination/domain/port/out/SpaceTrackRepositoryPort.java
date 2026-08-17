package com.corp.proyectospacetrafficcoordination.domain.port.out;

import com.corp.proyectospacetrafficcoordination.domain.model.LeoSatelliteTrack;
import java.util.List;
import java.util.Optional;

public interface SpaceTrackRepositoryPort {
    LeoSatelliteTrack save(LeoSatelliteTrack track);
    Optional<LeoSatelliteTrack> findById(String noradId);
    List<LeoSatelliteTrack> findAll();
}
