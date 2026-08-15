package com.corp.ecosystem.regenerative.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: TouristExperienceBooking (Turismo Regenerativo & Custodia Stripe Escrow).
 * <p>
 * Modela reservas de experiencias enoturísticas, agroturismo y actividades rurales con
 * custodia segura de fondos (Escrow) y liquidación automática tras validación por geovalla H3.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference Stripe Custom Connect & Escrow Architecture; WTO Regenerative Tourism Framework
 */
public record TouristExperienceBooking(
        BookingId id,
        String tenantId,
        String hostStripeAccountId,
        String travelerId,
        ExperienceDetails details,
        EscrowFinancials financials,
        GeofenceVerification verification,
        BookingState state,
        Instant createdAt
) implements Serializable {

    public record BookingId(String value) {
        public BookingId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("BookingId no puede estar vacío");
        }
    }

    public record ExperienceDetails(
            String experienceTitle,
            long h3LocationRes8,
            int attendeesCount,
            Instant scheduledStart
    ) {}

    public record EscrowFinancials(
            BigDecimal totalGrossEur,
            BigDecimal platformFeeEur,     // ej: 8% Take Rate
            BigDecimal netHostPayoutEur,
            boolean isFundsLockedInEscrow
    ) {}

    public record GeofenceVerification(
            boolean isCheckInValidated,
            long verifiedAtH3Res8,
            Instant validatedAt
    ) {}

    public enum BookingState {
        ESCROW_LOCKED, CHECKED_IN_AND_VALIDATED, SETTLED_TO_HOST, REFUNDED
    }

    public static TouristExperienceBooking createBooking(
            BookingId id,
            String tenantId,
            String hostStripeAccountId,
            String travelerId,
            ExperienceDetails details,
            BigDecimal pricePerAttendeeEur,
            double platformTakeRatePct
    ) {
        BigDecimal totalGross = pricePerAttendeeEur.multiply(BigDecimal.valueOf(details.attendeesCount()));
        BigDecimal fee = totalGross.multiply(BigDecimal.valueOf(platformTakeRatePct / 100.0));
        BigDecimal net = totalGross.subtract(fee);

        EscrowFinancials fin = new EscrowFinancials(totalGross, fee, net, true);
        GeofenceVerification geo = new GeofenceVerification(false, 0L, null);

        return new TouristExperienceBooking(
                id,
                tenantId,
                hostStripeAccountId,
                travelerId,
                details,
                fin,
                geo,
                BookingState.ESCROW_LOCKED,
                Instant.now()
        );
    }

    public TouristExperienceBooking validateCheckInAtLocation(long currentTouristH3Res8) {
        if (currentTouristH3Res8 != details.h3LocationRes8()) {
            throw new IllegalArgumentException("Ubicación GPS no coincide con la celda H3 de la experiencia");
        }

        GeofenceVerification geo = new GeofenceVerification(true, currentTouristH3Res8, Instant.now());
        EscrowFinancials fin = new EscrowFinancials(
                financials.totalGrossEur(),
                financials.platformFeeEur(),
                financials.netHostPayoutEur(),
                false // Fondos listos para transferir
        );

        return new TouristExperienceBooking(
                this.id,
                this.tenantId,
                this.hostStripeAccountId,
                this.travelerId,
                this.details,
                fin,
                geo,
                BookingState.SETTLED_TO_HOST,
                this.createdAt
        );
    }
}
