package com.corp.ecosystem.baggage.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: InterlineBaggageTwin (Conciliación Global de Equipajes IATA Resolution 753 y BLE/UWB).
 */
public record InterlineBaggageTwin(
        BaggageTagId id,
        String tenantId,
        String outboundFlightNumber,
        String connectingFlightNumber,
        BaggageHandlingMetrics metrics,
        BaggageTransferStatus status,
        Instant lastScannedAt
) implements Serializable {

    public record BaggageTagId(String value) {
        public BaggageTagId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("BaggageTagId no puede estar vacío");
        }
    }

    public record BaggageHandlingMetrics(
            int transferTimeRemainingMinutes,
            int minimumConnectingTimeMinutes,
            double rfidSignalStrengthDbm,
            boolean isAircraftHoldLoaded
    ) {
        public boolean isMishandledRisk() {
            return transferTimeRemainingMinutes < minimumConnectingTimeMinutes && !isAircraftHoldLoaded;
        }
    }

    public enum BaggageTransferStatus {
        ON_SCHEDULE_TRANSFER, EXPEDITED_APRON_RUNNER_ALERT, MISHANDLED_RECOVERY_TRIGGERED
    }

    public static InterlineBaggageTwin evaluateTransfer(
            BaggageTagId id,
            String tenantId,
            String outboundFlight,
            String connectingFlight,
            BaggageHandlingMetrics metrics
    ) {
        BaggageTransferStatus status = metrics.isMishandledRisk() ?
                BaggageTransferStatus.EXPEDITED_APRON_RUNNER_ALERT :
                BaggageTransferStatus.ON_SCHEDULE_TRANSFER;

        return new InterlineBaggageTwin(id, tenantId, outboundFlight, connectingFlight, metrics, status, Instant.now());
    }
}
