package com.corp.ecosystem.minerals.domain.port;

import com.corp.ecosystem.minerals.domain.BatteryMineralPassport;
import java.util.Optional;

public interface BatteryPassportRepositoryPort {
    BatteryMineralPassport save(BatteryMineralPassport passport);
    Optional<BatteryMineralPassport> findById(BatteryMineralPassport.PassportId id);
}
