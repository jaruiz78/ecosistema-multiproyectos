package com.corp.ecosystem.agroenergy.application;

import com.corp.ecosystem.agroenergy.domain.AgroEnergyCommunity;
import com.corp.ecosystem.agroenergy.domain.port.AgroEnergyRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgroEnergyVppService {

    private final AgroEnergyRepositoryPort repositoryPort;

    public AgroEnergyVppService(AgroEnergyRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public AgroEnergyCommunity registerCommunity(
            String tenantId,
            String name,
            double solarPeakKw,
            double solarCurrentKw,
            double batteryCapKwh,
            double batterySocPct,
            double maxBatteryKw,
            List<AgroEnergyCommunity.PumpStationLoad> pumpLoads
    ) {
        AgroEnergyCommunity.SolarParkSpecs solar = new AgroEnergyCommunity.SolarParkSpecs(solarPeakKw, solarCurrentKw, 0.92);
        AgroEnergyCommunity.BatteryStorageSpecs battery = new AgroEnergyCommunity.BatteryStorageSpecs(batteryCapKwh, batterySocPct, maxBatteryKw);

        AgroEnergyCommunity community = new AgroEnergyCommunity(
                new AgroEnergyCommunity.CommunityId("VPP-AGRO-" + System.nanoTime()),
                tenantId,
                name,
                solar,
                battery,
                pumpLoads != null ? List.copyOf(pumpLoads) : List.of(),
                AgroEnergyCommunity.CommunityEnergyState.AUTONOMOUS_SOLAR,
                Instant.now()
        );
        return repositoryPort.save(community);
    }

    public AgroEnergyCommunity.DispatchInstruction calculateDispatch(AgroEnergyCommunity.CommunityId id, double spotPriceEurMwh) {
        AgroEnergyCommunity comm = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comunidad no encontrada: " + id.value()));

        return comm.computeOptimalDispatch(spotPriceEurMwh);
    }

    public Optional<AgroEnergyCommunity> getCommunity(AgroEnergyCommunity.CommunityId id) {
        return repositoryPort.findById(id);
    }
}
