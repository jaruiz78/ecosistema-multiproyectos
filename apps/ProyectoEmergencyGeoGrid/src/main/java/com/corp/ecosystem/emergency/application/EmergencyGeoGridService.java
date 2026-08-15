package com.corp.ecosystem.emergency.application;

import com.corp.ecosystem.emergency.domain.EmergencyPerimeterTwin;
import com.corp.ecosystem.emergency.domain.port.EmergencyTwinRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmergencyGeoGridService {

    private final EmergencyTwinRepositoryPort repositoryPort;

    public EmergencyGeoGridService(EmergencyTwinRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public EmergencyPerimeterTwin declareEmergency(
            String tenantId,
            EmergencyPerimeterTwin.EmergencyType type,
            long initialH3CellRes8,
            double windSpeedKmH,
            double windDirDegrees,
            double tempCelsius
    ) {
        EmergencyPerimeterTwin.MeteorologicalVector weather = new EmergencyPerimeterTwin.MeteorologicalVector(
                windSpeedKmH, windDirDegrees, tempCelsius, 25.0
        );
        EmergencyPerimeterTwin.EvacuationAssessment evac = new EmergencyPerimeterTwin.EvacuationAssessment(
                350, List.of(initialH3CellRes8), 2, false
        );

        EmergencyPerimeterTwin emergency = new EmergencyPerimeterTwin(
                new EmergencyPerimeterTwin.EmergencyId("EMERGENCY-" + System.nanoTime()),
                tenantId,
                type,
                List.of(initialH3CellRes8),
                weather,
                evac,
                EmergencyPerimeterTwin.EmergencyLevel.SITUATION_1_LOCAL,
                Instant.now()
        );
        return repositoryPort.save(emergency);
    }

    public EmergencyPerimeterTwin updateFireSpread(
            EmergencyPerimeterTwin.EmergencyId id,
            double nextWindSpeed,
            double nextWindDir,
            List<Long> newIgnitedCells
    ) {
        EmergencyPerimeterTwin emergency = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emergencia no encontrada: " + id.value()));

        EmergencyPerimeterTwin updated = emergency.calculateNextSpread(nextWindSpeed, nextWindDir, newIgnitedCells);
        return repositoryPort.save(updated);
    }

    public Optional<EmergencyPerimeterTwin> getEmergency(EmergencyPerimeterTwin.EmergencyId id) {
        return repositoryPort.findById(id);
    }
}
