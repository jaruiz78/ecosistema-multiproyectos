package com.corp.ecosystem.pharmacold.application;

import com.corp.ecosystem.pharmacold.domain.PharmaShipmentBatch;
import com.corp.ecosystem.pharmacold.domain.port.PharmaBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PharmaColdChainService {

    private final PharmaBatchRepositoryPort repositoryPort;

    public PharmaColdChainService(PharmaBatchRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public PharmaShipmentBatch registerBatch(
            String tenantId,
            String drugName,
            PharmaShipmentBatch.DrugCategory category,
            double minTemp,
            double maxTemp,
            double maxAllowedLossPct,
            double activationEnergy
    ) {
        PharmaShipmentBatch.ThermalEnvelope envelope = new PharmaShipmentBatch.ThermalEnvelope(
                minTemp, maxTemp, maxAllowedLossPct, activationEnergy
        );

        PharmaShipmentBatch batch = new PharmaShipmentBatch(
                new PharmaShipmentBatch.BatchId("PHARMA-" + System.nanoTime()),
                tenantId,
                drugName,
                category,
                envelope,
                0.0,
                List.of(),
                PharmaShipmentBatch.BatchReleaseStatus.IN_TRANSIT_OPTIMAL,
                Instant.now()
        );
        return repositoryPort.save(batch);
    }

    public PharmaShipmentBatch recordTelemetry(PharmaShipmentBatch.BatchId id, double tempCelsius, double humidityPct) {
        PharmaShipmentBatch batch = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lote farmacéutico no encontrado: " + id.value()));

        PharmaShipmentBatch updated = batch.recordThermalReading(tempCelsius, humidityPct);
        return repositoryPort.save(updated);
    }

    public Optional<PharmaShipmentBatch> getBatch(PharmaShipmentBatch.BatchId id) {
        return repositoryPort.findById(id);
    }
}
