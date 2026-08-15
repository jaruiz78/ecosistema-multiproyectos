package com.corp.ecosystem.regenerative.domain.port;

import com.corp.ecosystem.regenerative.domain.TouristExperienceBooking;
import java.util.Optional;

public interface ExperienceBookingRepositoryPort {
    TouristExperienceBooking save(TouristExperienceBooking booking);
    Optional<TouristExperienceBooking> findById(TouristExperienceBooking.BookingId id);
}
