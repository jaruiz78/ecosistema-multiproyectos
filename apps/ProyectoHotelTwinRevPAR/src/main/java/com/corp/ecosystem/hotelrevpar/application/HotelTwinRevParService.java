package com.corp.ecosystem.hotelrevpar.application;

import com.corp.ecosystem.hotelrevpar.domain.HotelRoomTwinCluster;
import com.corp.ecosystem.hotelrevpar.domain.port.HotelTwinRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class HotelTwinRevParService {

    private final HotelTwinRepositoryPort repositoryPort;

    public HotelTwinRevParService(HotelTwinRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public HotelRoomTwinCluster registerHotel(
            String tenantId,
            String hotelName,
            int totalRooms,
            BigDecimal baseRateEur,
            double energyCostEurPerKwh
    ) {
        HotelRoomTwinCluster.RoomInventoryState inventory = new HotelRoomTwinCluster.RoomInventoryState(
                0, 0, totalRooms, baseRateEur.doubleValue()
        );
        HotelRoomTwinCluster.EnergyThermalProfile thermal = new HotelRoomTwinCluster.EnergyThermalProfile(
                25.0, 22.0, 0.0, energyCostEurPerKwh
        );
        HotelRoomTwinCluster.DynamicPricingStrategy pricing = new HotelRoomTwinCluster.DynamicPricingStrategy(
                baseRateEur, baseRateEur, 1.0, false
        );

        HotelRoomTwinCluster hotel = new HotelRoomTwinCluster(
                new HotelRoomTwinCluster.HotelId("HOTEL-" + System.nanoTime()),
                tenantId,
                hotelName,
                totalRooms,
                inventory,
                thermal,
                pricing,
                Instant.now()
        );
        return repositoryPort.save(hotel);
    }

    public HotelRoomTwinCluster optimizeHotelOperations(
            HotelRoomTwinCluster.HotelId id,
            int expectedArrivalsNext3Hours,
            double outdoorTempCelsius,
            double localH3DemandIndex
    ) {
        HotelRoomTwinCluster hotel = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel no encontrado: " + id.value()));

        HotelRoomTwinCluster updated = hotel.optimizePricingAndHvac(expectedArrivalsNext3Hours, outdoorTempCelsius, localH3DemandIndex);
        return repositoryPort.save(updated);
    }

    public Optional<HotelRoomTwinCluster> getHotel(HotelRoomTwinCluster.HotelId id) {
        return repositoryPort.findById(id);
    }
}
