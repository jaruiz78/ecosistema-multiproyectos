package com.corp.ecosystem.ecopassport;

import com.corp.ecosystem.ecopassport.application.EcoTourismPassportService;
import com.corp.ecosystem.ecopassport.domain.EcoTourismPassport;
import com.corp.ecosystem.ecopassport.domain.port.EcoPassportRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoEcoTourismPassport.
 */
class EcoTourismPassportTest {

    static class InMemoryEcoPassportRepository implements EcoPassportRepositoryPort {
        private final Map<EcoTourismPassport.PassportId, EcoTourismPassport> storage = new ConcurrentHashMap<>();

        @Override
        public EcoTourismPassport save(EcoTourismPassport passport) {
            storage.put(passport.id(), passport);
            return passport;
        }

        @Override
        public Optional<EcoTourismPassport> findById(EcoTourismPassport.PassportId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryEcoPassportRepository repository = new InMemoryEcoPassportRepository();
    private final EcoTourismPassportService service = new EcoTourismPassportService(repository);

    @Test
    @DisplayName("Debe emitir pasaporte con bonificación del 30% en la ecotasa por baja huella de carbono")
    void shouldApply30PctDiscountOnLowCarbonTrip() {
        EcoTourismPassport passport = service.issueEcoPassport(
                "dti-costa-brava",
                "BOOKING-LOW-CO2-99",
                "traveler-eco-01",
                "France",
                2, // 2 viajeros
                30.0, // 30 kg transporte (tren)
                20.0, // 20 kg hotel solar
                10.0, // 10 kg actividades
                4.0,  // 4 EUR base ecotasa por persona (Total: 8.00 EUR)
                "Restauracion de praderas de Posidonia Marina"
        );

        assertNotNull(passport.id());
        assertEquals(EcoTourismPassport.PassportState.OFFICIALLY_CERTIFIED, passport.state());
        assertEquals(60.0, passport.footprint().totalKgCo2()); // 30 kg / persona (< 50 kg)
        assertEquals(new BigDecimal("8.0"), passport.ecoTax().grossTaxEur());
        assertEquals(new BigDecimal("2.40"), passport.ecoTax().discountForLowCarbonTravelEur()); // 30% de 8.0
        assertEquals(new BigDecimal("5.60"), passport.ecoTax().netTaxPayableEur());
        assertTrue(passport.proofSeal().isVerifiedOnLedger());
    }

    @Test
    @DisplayName("Debe cobrar ecotasa completa sin descuento en viajes de alta emisión")
    void shouldChargeFullEcoTaxOnHighCarbonTrip() {
        EcoTourismPassport passport = service.issueEcoPassport(
                "dti-canarias",
                "BOOKING-HIGH-CO2-01",
                "traveler-02",
                "Germany",
                1,
                350.0, // 350 kg vuelo largo
                120.0,
                30.0,
                5.0, // 5.00 EUR
                "Reforestacion de Laurisilva Autoctona"
        );

        assertEquals(500.0, passport.footprint().totalKgCo2());
        assertEquals(new BigDecimal("5.0"), passport.ecoTax().grossTaxEur());
        assertEquals(BigDecimal.ZERO, passport.ecoTax().discountForLowCarbonTravelEur());
        assertEquals(new BigDecimal("5.0"), passport.ecoTax().netTaxPayableEur());
    }
}
