package com.corp.ecosystem.intermodal.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: IntermodalTransferHub (Sincronización de Cruceros/Vuelos & Despacho H3).
 * <p>
 * Modela la coordinación en tiempo real entre llegadas masivas de pasajeros en puertos y aeropuertos,
 * agrupamiento por celdas H3 de destino hotelero y despacho dinámico de flotas de transfers.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference IATA Passenger Standards; Cruise Lines International Association (CLIA) Intermodal Protocols
 */
public record IntermodalTransferHub(
        HubId id,
        String tenantId,
        String terminalName,
        HubType type,
        List<ArrivalEvent> scheduledArrivals,
        FleetAvailability fleet,
        List<TransferDispatchGroup> activeDispatches,
        Instant lastUpdated
) implements Serializable {

    public record HubId(String value) {
        public HubId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("HubId no puede estar vacío");
        }
    }

    public enum HubType {
        CRUISE_PORT_TERMINAL, AIRPORT_INTERNATIONAL, HIGH_SPEED_RAILWAY_STATION
    }

    public record ArrivalEvent(
            String carrierIdentifier, // ej: "CRUISE-MSC-WORLD-EUROPA" o "FLIGHT-IB3402"
            int arrivingPassengers,
            Instant estimatedDisembarkTime,
            boolean isDockedOrLanded
    ) {}

    public record FleetAvailability(
            int availableMinibuses16pax,
            int availableVans8pax,
            int availableTaxis4pax
    ) {
        public int totalCapacityPax() {
            return (availableMinibuses16pax * 16) + (availableVans8pax * 8) + (availableTaxis4pax * 4);
        }
    }

    public record TransferDispatchGroup(
            String dispatchId,
            long targetH3IndexRes8,
            String destinationHotelCluster,
            int allocatedPassengers,
            String assignedVehicleType,
            DispatchStatus status
    ) {}

    public enum DispatchStatus {
        BOARDING, DEPARTED, COMPLETED
    }

    public IntermodalTransferHub createDispatchForArrival(
            String carrierId,
            long targetH3IndexRes8,
            String destinationCluster,
            int passengerCount
    ) {
        String assignedVehicle = passengerCount > 8 ? "MINIBUS_16" : (passengerCount > 4 ? "VAN_8" : "TAXI_4");

        TransferDispatchGroup group = new TransferDispatchGroup(
                "DISP-" + System.nanoTime(),
                targetH3IndexRes8,
                destinationCluster,
                passengerCount,
                assignedVehicle,
                DispatchStatus.BOARDING
        );

        List<TransferDispatchGroup> dispatches = new java.util.ArrayList<>(this.activeDispatches);
        dispatches.add(group);

        return new IntermodalTransferHub(
                this.id,
                this.tenantId,
                this.terminalName,
                this.type,
                this.scheduledArrivals,
                this.fleet,
                List.copyOf(dispatches),
                Instant.now()
        );
    }
}
