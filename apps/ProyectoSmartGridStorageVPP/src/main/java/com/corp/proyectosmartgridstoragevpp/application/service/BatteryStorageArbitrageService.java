package com.corp.proyectosmartgridstoragevpp.application.service;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import com.corp.proyectosmartgridstoragevpp.domain.port.out.BatteryStorageRepositoryPort;

public class BatteryStorageArbitrageService {

    private final BatteryStorageRepositoryPort repositoryPort;

    public BatteryStorageArbitrageService(BatteryStorageRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public BatteryStorageUnit executeIntradayArbitrage(String batteryId, double marketPriceEurMwh, double priceThresholdEurMwh, double dispatchHours) {
        BatteryStorageUnit unit = repositoryPort.findById(batteryId)
                .orElseGet(() -> BatteryStorageUnit.create(batteryId, 1000.0, 250.0));

        double dispatchEnergyKwh = Math.min(unit.capacityKwh() * 0.25, unit.maxCrateChargeKw() * dispatchHours);

        BatteryStorageUnit updated;
        if (marketPriceEurMwh < priceThresholdEurMwh && unit.currentSocPct() < 90.0) {
            // Precio bajo -> Cargar batería
            updated = unit.charge(dispatchEnergyKwh, unit.maxCrateChargeKw());
        } else if (marketPriceEurMwh >= priceThresholdEurMwh && unit.currentSocPct() > 20.0) {
            // Precio alto -> Descargar batería
            updated = unit.discharge(dispatchEnergyKwh, unit.maxCrateDischargeKw());
        } else {
            updated = unit;
        }

        return repositoryPort.save(updated);
    }
}
