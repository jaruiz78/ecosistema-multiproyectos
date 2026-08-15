package com.corp.ecosystem.airport.domain.port;

import com.corp.ecosystem.airport.domain.AirportIntermodalPassengerTransfer;
import java.util.Optional;

public interface AirportTransferRepositoryPort {
    AirportIntermodalPassengerTransfer save(AirportIntermodalPassengerTransfer transfer);
    Optional<AirportIntermodalPassengerTransfer> findById(AirportIntermodalPassengerTransfer.TransferId id);
}
