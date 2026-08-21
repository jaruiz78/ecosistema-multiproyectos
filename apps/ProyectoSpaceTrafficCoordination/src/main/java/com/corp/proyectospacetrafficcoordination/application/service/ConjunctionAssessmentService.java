package com.corp.proyectospacetrafficcoordination.application.service;

import com.corp.proyectospacetrafficcoordination.domain.model.LeoSatelliteTrack;
import com.corp.proyectospacetrafficcoordination.domain.port.out.SpaceTrackRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ConjunctionAssessmentService {

    private final SpaceTrackRepositoryPort repositoryPort;

    public ConjunctionAssessmentService(SpaceTrackRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public LeoSatelliteTrack assessConjunctionRisk(String primaryId, String debrisId) {
        LeoSatelliteTrack primary = repositoryPort.findById(primaryId)
                .orElseGet(() -> LeoSatelliteTrack.createActive(primaryId, "PRIMARY_SAT_01", 550.0, 53.0));
        LeoSatelliteTrack debris = repositoryPort.findById(debrisId)
                .orElseGet(() -> LeoSatelliteTrack.createActive(debrisId, "DEBRIS_PIECE_99", 550.8, 53.2));

        LeoSatelliteTrack assessed = primary.evaluateConjunction(debris);
        return repositoryPort.save(assessed);
    }
}
