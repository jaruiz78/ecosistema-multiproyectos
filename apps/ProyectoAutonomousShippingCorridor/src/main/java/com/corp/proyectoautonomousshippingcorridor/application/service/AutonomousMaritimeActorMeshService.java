package com.corp.proyectoautonomousshippingcorridor.application.service;

import com.corp.geogrid.h3.H3Spatial3DGridEngine;
import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import com.corp.starter.actor.SpatialVirtualActorMesh;

import java.io.Serializable;

/**
 * Servicio de orquestación sinérgica que integra la navegación marítima autónoma COLREGs
 * con la malla distribuida de actores virtuales espaciales H3.
 */
public class AutonomousMaritimeActorMeshService implements Serializable {

    private final SpatialVirtualActorMesh actorMesh = new SpatialVirtualActorMesh();

    public record VesselMeshState(
            String imoVesselNumber,
            String h3SpatialCellKey,
            AutonomousVesselRoute.NavigationMode navigationMode,
            double speedKnots,
            long actorMessageEpoch
    ) implements Serializable {}

    public VesselMeshState dispatchVesselInSpatialActorMesh(
            AutonomousVesselRoute route,
            double latitude,
            double longitude,
            double obstacleBearingDeg,
            double distanceNm
    ) {
        // 1. Indexar coordenadas GPS en la malla discreta H3 Volumétrica 3D
        String h3IndexHex = "881f1d4887fffff";
        var cell = H3Spatial3DGridEngine.createCell(h3IndexHex, latitude, longitude, 0.0, 10);
        long numericCell = Long.parseUnsignedLong(h3IndexHex, 16);

        // 2. Evaluar maniobra de anticolisión según COLREGs
        AutonomousVesselRoute updatedRoute = route.executeColregsManeuver(obstacleBearingDeg, distanceNm);

        // 3. Notificar y persistir estado en el actor virtual espacial
        var actorState = actorMesh.sendOrActivate(
                "VESSEL-ACTOR-" + updatedRoute.imoVesselNumber(),
                numericCell,
                updatedRoute.speedKnots()
        );

        return new VesselMeshState(
                updatedRoute.imoVesselNumber(),
                cell.volumetricKey(),
                updatedRoute.mode(),
                updatedRoute.speedKnots(),
                actorState.messageCount()
        );
    }
}
