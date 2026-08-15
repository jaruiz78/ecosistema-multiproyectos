package com.corp.ecosystem.regenerative;

import com.corp.ecosystem.regenerative.application.RegenerativeExperienceService;
import com.corp.ecosystem.regenerative.domain.TouristExperienceBooking;
import com.corp.ecosystem.regenerative.domain.port.ExperienceBookingRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoRegenerativeExperience.
 */
class TouristExperienceBookingTest {

    static class InMemoryBookingRepository implements ExperienceBookingRepositoryPort {
        private final Map<TouristExperienceBooking.BookingId, TouristExperienceBooking> storage = new ConcurrentHashMap<>();

        @Override
        public TouristExperienceBooking save(TouristExperienceBooking booking) {
            storage.put(booking.id(), booking);
            return booking;
        }

        @Override
        public Optional<TouristExperienceBooking> findById(TouristExperienceBooking.BookingId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryBookingRepository repository = new InMemoryBookingRepository();
    private final RegenerativeExperienceService service = new RegenerativeExperienceService(repository);

    @Test
    @DisplayName("Debe crear una reserva con fondos retenidos en Escrow y comisión del 8%")
    void shouldCreateBookingWithLockedEscrow() {
        TouristExperienceBooking booking = service.bookExperience(
                "agro-turismo-andalucia",
                "acct_stripe_bodega_ronda_01",
                "traveler-wine-lover",
                "Cata Enoturistica en Bodega Biodinamica",
                0x88390cb307fffffL,
                4, // 4 personas
                new BigDecimal("60.00"), // 60 EUR por persona (Total: 240 EUR)
                Instant.now().plusSeconds(86400 * 2)
        );

        assertNotNull(booking.id());
        assertEquals(TouristExperienceBooking.BookingState.ESCROW_LOCKED, booking.state());
        assertEquals(new BigDecimal("240.00"), booking.financials().totalGrossEur());
        assertEquals(new BigDecimal("19.2000"), booking.financials().platformFeeEur()); // 8% de 240
        assertEquals(new BigDecimal("220.8000"), booking.financials().netHostPayoutEur());
        assertTrue(booking.financials().isFundsLockedInEscrow());
    }

    @Test
    @DisplayName("Debe liberar el pago Escrow tras validación geográfica por geovalla H3")
    void shouldReleaseEscrowOnH3GeofenceMatch() {
        long bodegaH3 = 0x88390cb307fffffL;
        TouristExperienceBooking booking = service.bookExperience(
                "rural-experience-galicia",
                "acct_stripe_queixeria_01",
                "traveler-foodie",
                "Taller Artesanal de Queso Cebreiro",
                bodegaH3,
                2,
                new BigDecimal("45.00"),
                Instant.now().plusSeconds(3600)
        );

        // Check-in del turista con GPS en la misma celda H3 de la quesería
        TouristExperienceBooking updated = service.validateAndReleaseEscrow(booking.id(), bodegaH3);

        assertEquals(TouristExperienceBooking.BookingState.SETTLED_TO_HOST, updated.state());
        assertFalse(updated.financials().isFundsLockedInEscrow());
        assertTrue(updated.verification().isCheckInValidated());
    }
}
