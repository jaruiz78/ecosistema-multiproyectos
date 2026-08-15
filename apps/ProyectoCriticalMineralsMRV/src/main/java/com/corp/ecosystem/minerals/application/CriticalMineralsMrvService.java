package com.corp.ecosystem.minerals.application;

import com.corp.ecosystem.minerals.domain.BatteryMineralPassport;
import com.corp.ecosystem.minerals.domain.port.BatteryPassportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CriticalMineralsMrvService {

    private final BatteryPassportRepositoryPort repositoryPort;

    public CriticalMineralsMrvService(BatteryPassportRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public BatteryMineralPassport certifyBattery(
            String tenantId,
            String serialNumber,
            double lithiumKg,
            double cobaltKg,
            double nickelKg,
            double recycledLithiumPct,
            double recycledCobaltPct,
            double recycledNickelPct,
            double carbonKgPerKwh
    ) {
        BatteryMineralPassport.PassportId id = new BatteryMineralPassport.PassportId("BATTPASS-" + System.nanoTime());
        BatteryMineralPassport.MineralComposition composition = new BatteryMineralPassport.MineralComposition(
                lithiumKg, cobaltKg, nickelKg, recycledLithiumPct, recycledCobaltPct, recycledNickelPct
        );
        BatteryMineralPassport.RefiningCarbonFootprint footprint = new BatteryMineralPassport.RefiningCarbonFootprint(
                12.5, 18.0, carbonKgPerKwh
        );

        BatteryMineralPassport passport = BatteryMineralPassport.issuePassport(id, tenantId, serialNumber, composition, footprint);
        return repositoryPort.save(passport);
    }

    public Optional<BatteryMineralPassport> getPassport(BatteryMineralPassport.PassportId id) {
        return repositoryPort.findById(id);
    }
}
