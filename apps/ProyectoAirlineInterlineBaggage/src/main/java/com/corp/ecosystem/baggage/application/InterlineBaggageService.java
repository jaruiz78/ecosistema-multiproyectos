package com.corp.ecosystem.baggage.application;

import com.corp.ecosystem.baggage.domain.InterlineBaggageTwin;
import com.corp.ecosystem.baggage.domain.port.InterlineBaggageRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class InterlineBaggageService {

    private final InterlineBaggageRepositoryPort repositoryPort;

    public InterlineBaggageService(InterlineBaggageRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public InterlineBaggageTwin processBaggageTransfer(
            String tenantId,
            String outboundFlight,
            String connectingFlight,
            int timeRemainingMin,
            int minConnectingMin,
            double rfidSignalDbm,
            boolean isLoaded
    ) {
        InterlineBaggageTwin.BaggageTagId id = new InterlineBaggageTwin.BaggageTagId("BAG-" + System.nanoTime());
        InterlineBaggageTwin.BaggageHandlingMetrics metrics = new InterlineBaggageTwin.BaggageHandlingMetrics(
                timeRemainingMin, minConnectingMin, rfidSignalDbm, isLoaded
        );
        InterlineBaggageTwin twin = InterlineBaggageTwin.evaluateTransfer(id, tenantId, outboundFlight, connectingFlight, metrics);
        return repositoryPort.save(twin);
    }

    public Optional<InterlineBaggageTwin> getBaggage(InterlineBaggageTwin.BaggageTagId id) {
        return repositoryPort.findById(id);
    }
}
