package com.corp.agro.robotics.domain;

import java.util.List;
import java.util.Objects;

/**
 * Servicio de Despacho y Coordinación Descentralizada de Enjambres Agro-Biológicos.
 * Asigna celdas espaciales Uber H3 para polinización dirigida y micro-aplicación de bio-tratamientos.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class SwarmRoboticsDispatchService {

    public record SwarmMissionPlan(
            String missionId,
            String targetParcelId,
            int activeDronesCount,
            int totalFlowersPollinated,
            double batteryConsumedAvg,
            double coverageHectares
    ) {}

    public SwarmMissionPlan dispatchPollinationMission(String missionId, String parcelId, List<BioDroneSwarmNode> swarm) {
        Objects.requireNonNull(missionId, "missionId no puede ser nulo");
        Objects.requireNonNull(parcelId, "parcelId no puede ser nulo");
        Objects.requireNonNull(swarm, "enjambre no puede ser nulo");

        List<BioDroneSwarmNode> activeDrones = swarm.stream()
                .filter(BioDroneSwarmNode::canDeployMission)
                .toList();

        if (activeDrones.isEmpty()) {
            return new SwarmMissionPlan(missionId, parcelId, 0, 0, 0.0, 0.0);
        }

        int totalFlowers = activeDrones.stream().mapToInt(BioDroneSwarmNode::flowersPollinatedCount).sum();
        double avgBattery = activeDrones.stream().mapToDouble(BioDroneSwarmNode::batteryPercent).average().orElse(0.0);
        double coverage = activeDrones.size() * 0.25; // 0.25 hectáreas por micro-robot

        return new SwarmMissionPlan(
                missionId, parcelId, activeDrones.size(),
                totalFlowers, Math.round((100.0 - avgBattery) * 10.0) / 10.0,
                coverage
        );
    }
}
