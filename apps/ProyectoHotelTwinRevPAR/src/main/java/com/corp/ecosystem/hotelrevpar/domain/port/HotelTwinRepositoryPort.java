package com.corp.ecosystem.hotelrevpar.domain.port;

import com.corp.ecosystem.hotelrevpar.domain.HotelRoomTwinCluster;
import java.util.Optional;

public interface HotelTwinRepositoryPort {
    HotelRoomTwinCluster save(HotelRoomTwinCluster hotel);
    Optional<HotelRoomTwinCluster> findById(HotelRoomTwinCluster.HotelId id);
}
