package com.corp.proyectodroneairspace.application;

import com.corp.eventmesh.nats.EventMeshNatsPublisher;
import com.corp.eventmesh.nats.domain.NatsEventPayload;
import com.corp.geogrid.h3.H3Spatial3DGridEngine;
import com.corp.geogrid.h3.H3Spatial3DGridEngine.H3VolumetricCell;
import com.corp.geogrid.h3.H3Spatial3DGridEngine.Spatial3DVector;
import com.corp.proyectodroneairspace.domain.IntermodalMobilityPlan;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Sinergia Cruzada para Orquestación Intermodal Aéreo-Terrestre.
 *
 * <p>Integra:
 * <ul>
 *   <li>Motor de indexación volumétrica 3D H3 ({@link H3Spatial3DGridEngine}).</li>
 *   <li>Malla de mensajería reactiva NATS JetStream de latencia sub-milisegundo ({@link EventMeshNatsPublisher}).</li>
 * </ul>
 *
 * @see docs/formacion_ecosistema/modulo_8_ingenieria_geoespacial_h3_osrm/01_indexacion_h3_y_routing.md
 * @see docs/adr/adr-002-uber-h3-spatial-indexing.md
 */
public final class IntermodalAirGroundMobilityOrchestratorService {

    private final EventMeshNatsPublisher natsPublisher;
    private final ReentrantLock lock = new ReentrantLock();

    public IntermodalAirGroundMobilityOrchestratorService(EventMeshNatsPublisher natsPublisher) {
        this.natsPublisher = Objects.requireNonNull(natsPublisher, "natsPublisher no puede ser nulo");
    }

    public IntermodalAirGroundMobilityOrchestratorService() {
        this(new EventMeshNatsPublisher());
    }

    /**
     * Planifica una ruta intermodal combinando cálculo volumétrico 3D y notificación de baja latencia por NATS.
     *
     * @param originH3      Hexágono H3 2D origen
     * @param originLat     Latitud origen
     * @param originLon     Longitud origen
     * @param originAlt     Altitud superficie origen en metros
     * @param destH3        Hexágono H3 2D destino
     * @param destLat       Latitud destino
     * @param destLon       Longitud destino
     * @param destAlt       Altitud superficie destino en metros
     * @param flightCeiling Band step / Altitud crucero del dron en metros
     * @return {@link IntermodalMobilityPlan}
     */
    public IntermodalMobilityPlan planIntermodalRoute(
            String originH3,
            double originLat,
            double originLon,
            double originAlt,
            String destH3,
            double destLat,
            double destLon,
            double destAlt,
            int flightCeiling
    ) {
        Objects.requireNonNull(originH3, "originH3 no puede ser nulo");
        Objects.requireNonNull(destH3, "destH3 no puede ser nulo");

        lock.lock();
        try {
            H3VolumetricCell originCell = H3Spatial3DGridEngine.createCell(originH3, originLat, originLon, originAlt, 50);
            H3VolumetricCell destCell = H3Spatial3DGridEngine.createCell(destH3, destLat, destLon, destAlt + flightCeiling, 50);

            Spatial3DVector vector3D = H3Spatial3DGridEngine.calculate3DVector(originCell, destCell);

            // Estimaciones de velocidad: Drone ~60 km/h en línea recta 3D, Vehículo terrestre ~35 km/h con factor de tortuosidad 1.35
            double distance3Dkm = Math.max(0.1, vector3D.distance3Dkm());
            double droneMinutes = (distance3Dkm / 60.0) * 60.0;
            double groundMinutes = ((vector3D.distance2Dkm() * 1.35) / 35.0) * 60.0;

            String planId = "INTERMODAL-" + UUID.randomUUID().toString().substring(0, 8);

            // Publicar evento en NATS JetStream mesh para sincronizar AppViajes y ProyectoLogistica
            String eventJson = String.format(
                    "{\"planId\":\"%s\",\"origin\":\"%s\",\"dest\":\"%s\",\"dist3D\":%.3f,\"droneMins\":%.2f}",
                    planId, originH3, destH3, distance3Dkm, droneMinutes
            );
            NatsEventPayload natsEvent = new NatsEventPayload(
                    "mobility.intermodal.dispatch",
                    "tenant-intermodal-hub",
                    eventJson,
                    System.currentTimeMillis()
            );

            boolean published = natsPublisher.publish(natsEvent);

            return new IntermodalMobilityPlan(
                    planId,
                    originH3,
                    destH3,
                    destCell.altitudeBandMeters(),
                    distance3Dkm,
                    droneMinutes,
                    groundMinutes,
                    published
            );
        } finally {
            lock.unlock();
        }
    }
}
