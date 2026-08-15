package com.corp.ecosystem.regenerative.application;

import com.corp.ecosystem.regenerative.domain.TouristExperienceBooking;
import com.corp.ecosystem.regenerative.domain.port.ExperienceBookingRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class RegenerativeExperienceService {

    private final ExperienceBookingRepositoryPort repositoryPort;

    public RegenerativeExperienceService(ExperienceBookingRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public TouristExperienceBooking bookExperience(
            String tenantId,
            String hostStripeAccountId,
            String travelerId,
            String experienceTitle,
            long h3LocationRes8,
            int attendeesCount,
            BigDecimal pricePerAttendeeEur,
            Instant scheduledStart
    ) {
        TouristExperienceBooking.BookingId id = new TouristExperienceBooking.BookingId("BOOK-" + System.nanoTime());
        TouristExperienceBooking.ExperienceDetails details = new TouristExperienceBooking.ExperienceDetails(
                experienceTitle, h3LocationRes8, attendeesCount, scheduledStart
        );

        TouristExperienceBooking booking = TouristExperienceBooking.createBooking(
                id,
                tenantId,
                hostStripeAccountId,
                travelerId,
                details,
                pricePerAttendeeEur,
                8.0 // 8% Take Rate corporativo
        );

        return repositoryPort.save(booking);
    }

    public TouristExperienceBooking validateAndReleaseEscrow(TouristExperienceBooking.BookingId id, long currentTouristH3Res8) {
        TouristExperienceBooking booking = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + id.value()));

        TouristExperienceBooking updated = booking.validateCheckInAtLocation(currentTouristH3Res8);
        return repositoryPort.save(updated);
    }

    public Optional<TouristExperienceBooking> getBooking(TouristExperienceBooking.BookingId id) {
        return repositoryPort.findById(id);
    }
}
