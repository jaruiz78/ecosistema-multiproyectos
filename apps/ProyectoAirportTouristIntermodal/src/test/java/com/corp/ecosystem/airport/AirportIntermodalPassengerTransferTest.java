package com.corp.ecosystem.airport;

import com.corp.ecosystem.airport.application.AirportTransferService;
import com.corp.ecosystem.airport.domain.AirportIntermodalPassengerTransfer;
import com.corp.ecosystem.airport.domain.port.AirportTransferRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AirportIntermodalPassengerTransferTest {

    static class InMemoryAirportTransferRepository implements AirportTransferRepositoryPort {
        private final Map<AirportIntermodalPassengerTransfer.TransferId, AirportIntermodalPassengerTransfer> storage = new ConcurrentHashMap<>();

        @Override
        public AirportIntermodalPassengerTransfer save(AirportIntermodalPassengerTransfer transfer) {
            storage.put(transfer.id(), transfer);
            return transfer;
        }

        @Override
        public Optional<AirportIntermodalPassengerTransfer> findById(AirportIntermodalPassengerTransfer.TransferId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryAirportTransferRepository repository = new InMemoryAirportTransferRepository();
    private final AirportTransferService service = new AirportTransferService(repository);

    @Test
    @DisplayName("Debe confirmar transfer intermodal a tiempo con tiempo de conexión superior al MCT")
    void shouldConfirmTransferOnTime() {
        AirportIntermodalPassengerTransfer transfer = service.scheduleTransfer(
                "aena-adif-intermodal",
                "IB3150",
                "AVE02140",
                2,
                65.0 // 65 minutos (>45 MCT)
        );

        assertNotNull(transfer.id());
        assertEquals(AirportIntermodalPassengerTransfer.TransferStatus.TRANSFER_CONFIRMED_ON_TIME, transfer.status());
        assertTrue(transfer.luggageProfile().isMctSatisfied());
    }
}
