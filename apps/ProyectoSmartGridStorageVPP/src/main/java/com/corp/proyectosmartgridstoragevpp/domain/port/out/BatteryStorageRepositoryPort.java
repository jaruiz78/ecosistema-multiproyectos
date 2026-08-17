package com.corp.proyectosmartgridstoragevpp.domain.port.out;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import java.util.Optional;

public interface BatteryStorageRepositoryPort {
    BatteryStorageUnit save(BatteryStorageUnit unit);
    Optional<BatteryStorageUnit> findById(String batteryId);
}
