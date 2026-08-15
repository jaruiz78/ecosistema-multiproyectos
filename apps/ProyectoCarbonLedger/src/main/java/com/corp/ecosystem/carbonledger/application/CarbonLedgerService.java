package com.corp.ecosystem.carbonledger.application;

import com.corp.ecosystem.carbonledger.domain.DigitalProductPassport;
import com.corp.ecosystem.carbonledger.domain.port.PassportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Caso de Uso de Aplicación: Orquestación de Certificación de Pasaportes Digitales.
 */
@Service
public class CarbonLedgerService {

    private final PassportRepositoryPort repositoryPort;

    public CarbonLedgerService(PassportRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public DigitalProductPassport createDraftPassport(
            String tenantId,
            String batchIdentifier,
            DigitalProductPassport.ProductCategory category,
            double rawCo2,
            double mfgCo2,
            double logCo2,
            double avoidedCo2,
            double recycledPct,
            double recyclabilityPct,
            int lifespanMonths
    ) {
        DigitalProductPassport.CarbonFootprint footprint = DigitalProductPassport.CarbonFootprint.compute(
                rawCo2, mfgCo2, logCo2, avoidedCo2
        );
        DigitalProductPassport.CircularMetrics metrics = new DigitalProductPassport.CircularMetrics(
                recycledPct, recyclabilityPct, lifespanMonths
        );
        DigitalProductPassport passport = new DigitalProductPassport(
                new DigitalProductPassport.PassportId("DPP-" + System.nanoTime()),
                tenantId,
                batchIdentifier,
                category,
                footprint,
                metrics,
                null,
                DigitalProductPassport.PassportState.DRAFT,
                null
        );
        return repositoryPort.save(passport);
    }

    public DigitalProductPassport certifyPassport(DigitalProductPassport.PassportId id, String merkleRoot, String snarkProof, String authority) {
        DigitalProductPassport existing = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pasaporte no encontrado: " + id.value()));

        DigitalProductPassport.ZkProofSeal seal = new DigitalProductPassport.ZkProofSeal(merkleRoot, snarkProof, authority);
        DigitalProductPassport certified = existing.certify(seal);
        return repositoryPort.save(certified);
    }

    public Optional<DigitalProductPassport> getPassport(DigitalProductPassport.PassportId id) {
        return repositoryPort.findById(id);
    }
}
