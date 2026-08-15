package com.corp.ecosystem.airport.application;

import com.corp.ecosystem.airport.domain.AirportIntermodalPassengerTransfer;
import com.corp.ecosystem.airport.domain.port.AirportTransferRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class AirportTransferService {

    private final AirportTransferRepositoryPort repositoryPort;

    public AirportTransferService(AirportTransferRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public AirportIntermodalPassengerTransfer scheduleTransfer(
            String tenantId,
            String flightIata,
            String trainRenfe,
            int bagsCount,
            double connectionMinutes
    ) {
        AirportIntermodalPassengerTransfer.TransferId id = new AirportIntermodalPassengerTransfer.TransferId("TRANSFER-" + System.nanoTime());
        AirportIntermodalPassengerTransfer.PassengerLuggageProfile profile = new AirportIntermodalPassengerTransfer.PassengerLuggageProfile(
                bagsCount, true, true, connectionMinutes
        );
        AirportIntermodalPassengerTransfer transfer = AirportIntermodalPassengerTransfer.createTransfer(
                id, tenantId, flightIata, trainRenfe, profile, Instant.now()
        );
        return repositoryPort.save(transfer);
    }

    public Optional<AirportIntermodalPassengerTransfer> getTransfer(AirportIntermodalPassengerTransfer.TransferId id) {
        return repositoryPort.findById(id);
    }
}
