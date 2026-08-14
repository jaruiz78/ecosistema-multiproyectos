package com.proyecto.airdefense.application;

import com.proyecto.airdefense.domain.TacticalThreatRadar;
import java.util.List;
import java.util.Objects;

/**
 * Servicio táctico de evaluación y respuesta de defensa en red mallada air-gapped.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class TacticalAirDefenseService {

    public record TacticalEngagement(String targetTrackId, int threatLevel, String recommendedInterceptionH3Cell, boolean immediateActionRequired) {}

    public List<TacticalEngagement> evaluateThreatMesh(List<TacticalThreatRadar> activeTracks) {
        if (activeTracks == null || activeTracks.isEmpty()) return List.of();

        return activeTracks.stream()
                .filter(t -> t.computeThreatPriorityLevel() > 0)
                .map(t -> {
                    int priority = t.computeThreatPriorityLevel();
                    boolean immediate = priority >= 3;
                    return new TacticalEngagement(t.targetTrackId(), priority, t.h3HexCell(), immediate);
                })
                .sorted((a, b) -> Integer.compare(b.threatLevel(), a.threatLevel()))
                .toList();
    }
}
