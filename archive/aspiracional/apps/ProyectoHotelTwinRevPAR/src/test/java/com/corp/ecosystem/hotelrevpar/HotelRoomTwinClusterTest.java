package com.corp.ecosystem.hotelrevpar;

import com.corp.ecosystem.hotelrevpar.application.HotelTwinRevParService;
import com.corp.ecosystem.hotelrevpar.domain.HotelRoomTwinCluster;
import com.corp.ecosystem.hotelrevpar.domain.port.HotelTwinRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoHotelTwinRevPAR.
 */
class HotelRoomTwinClusterTest {

    static class InMemoryHotelRepository implements HotelTwinRepositoryPort {
        private final Map<HotelRoomTwinCluster.HotelId, HotelRoomTwinCluster> storage = new ConcurrentHashMap<>();

        @Override
        public HotelRoomTwinCluster save(HotelRoomTwinCluster hotel) {
            storage.put(hotel.id(), hotel);
            return hotel;
        }

        @Override
        public Optional<HotelRoomTwinCluster> findById(HotelRoomTwinCluster.HotelId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryHotelRepository repository = new InMemoryHotelRepository();
    private final HotelTwinRevParService service = new HotelTwinRevParService(repository);

    @Test
    @DisplayName("Debe registrar un hotel con tarifa base y climatización pasiva inicial")
    void shouldRegisterHotelWithBaseRate() {
        HotelRoomTwinCluster hotel = service.registerHotel(
                "hotel-resort-marbella",
                "Grand Luxury Resort & Spa",
                200, // 200 habitaciones
                new BigDecimal("180.00"),
                0.22 // 0.22 EUR/kWh
        );

        assertNotNull(hotel.id());
        assertEquals(200, hotel.totalRooms());
        assertEquals(180.0, hotel.pricingStrategy().currentSuggestedRateEur().doubleValue());
        assertFalse(hotel.pricingStrategy().isPreCoolingActive());
    }

    @Test
    @DisplayName("Debe incrementar tarifa por alta demanda H3 y activar pre-climatización ante ola de calor")
    void shouldIncreaseRateAndTriggerPreCoolingOnHeatwaveAndHighDemand() {
        HotelRoomTwinCluster hotel = service.registerHotel(
                "hotel-ibiza-bay",
                "Ibiza Bay Eco Resort",
                100,
                new BigDecimal("250.00"),
                0.28
        );

        // Ola de calor (36°C) + 30 llegadas inminentes (> 20% habitaciones) + alta demanda H3 (1.8)
        HotelRoomTwinCluster updated = service.optimizeHotelOperations(hotel.id(), 30, 36.0, 1.8);

        assertTrue(updated.pricingStrategy().currentSuggestedRateEur().doubleValue() > 250.00);
        assertTrue(updated.pricingStrategy().isPreCoolingActive());
        assertEquals(36.0, updated.thermalProfile().outdoorTempCelsius());
        assertEquals(36.0, updated.thermalProfile().currentHvacPowerKw()); // 30 llegadas * 1.2 kW
    }
}
