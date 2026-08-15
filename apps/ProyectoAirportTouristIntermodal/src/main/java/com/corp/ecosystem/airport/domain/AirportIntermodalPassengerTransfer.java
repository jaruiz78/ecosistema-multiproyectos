package com.corp.ecosystem.airport.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: AirportIntermodalPassengerTransfer (Intermodalidad Aeropuerto - AVE / Flujo de Pasajeros).
 */
public record AirportIntermodalPassengerTransfer(
        TransferId id,
        String tenantId,
        String flightIataCode,
        String trainRenfeCode,
        PassengerLuggageProfile luggageProfile,
        TransferStatus status,
        Instant scheduledTransferTime
) implements Serializable {

    public record TransferId(String value) {
        public TransferId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("TransferId no puede estar vacío");
        }
    }

    public record PassengerLuggageProfile(
            int checkedBagsCount,
            boolean isRfidTracked,
            boolean isBiometricBoardingAuthorized,
            double connectionTimeMinutes
    ) {
        public boolean isMctSatisfied() {
            // Minimum Connection Time (MCT): Mínimo 45 minutos para transfer avión-tren
            return connectionTimeMinutes >= 45.0;
        }
    }

    public enum TransferStatus {
        TRANSFER_CONFIRMED_ON_TIME, TIGHT_CONNECTION_EXPEDITE, CONNECTION_MISSED_REROUTED
    }

    public static AirportIntermodalPassengerTransfer createTransfer(
            TransferId id,
            String tenantId,
            String flight,
            String train,
            PassengerLuggageProfile profile,
            Instant time
    ) {
        TransferStatus status = profile.isMctSatisfied() ?
                TransferStatus.TRANSFER_CONFIRMED_ON_TIME :
                (profile.connectionTimeMinutes() >= 25.0 ?
                        TransferStatus.TIGHT_CONNECTION_EXPEDITE :
                        TransferStatus.CONNECTION_MISSED_REROUTED);

        return new AirportIntermodalPassengerTransfer(id, tenantId, flight, train, profile, status, time);
    }
}
