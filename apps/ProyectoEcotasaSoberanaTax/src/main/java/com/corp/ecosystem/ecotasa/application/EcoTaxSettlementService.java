package com.corp.ecosystem.ecotasa.application;

import com.corp.ecosystem.ecotasa.domain.RegionalEcoTaxSettlement;
import com.corp.ecosystem.ecotasa.domain.port.EcoTaxSettlementRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class EcoTaxSettlementService {

    private final EcoTaxSettlementRepositoryPort repositoryPort;

    public EcoTaxSettlementService(EcoTaxSettlementRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public RegionalEcoTaxSettlement settleEcoTax(
            String autonomousCommunityId,
            String accommodationId,
            int guestNights,
            double ratePerNight,
            String targetProject,
            String zkProof
    ) {
        RegionalEcoTaxSettlement.SettlementId id = new RegionalEcoTaxSettlement.SettlementId("ECOTAX-" + System.nanoTime());
        RegionalEcoTaxSettlement settlement = RegionalEcoTaxSettlement.createSettlement(
                id, autonomousCommunityId, accommodationId, guestNights, ratePerNight, targetProject, zkProof
        );
        return repositoryPort.save(settlement);
    }

    public Optional<RegionalEcoTaxSettlement> getSettlement(RegionalEcoTaxSettlement.SettlementId id) {
        return repositoryPort.findById(id);
    }
}
