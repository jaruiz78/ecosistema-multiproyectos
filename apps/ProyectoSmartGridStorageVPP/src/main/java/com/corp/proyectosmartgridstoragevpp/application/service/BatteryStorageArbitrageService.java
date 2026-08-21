package com.corp.proyectosmartgridstoragevpp.application.service;

import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import com.corp.proyectosmartgridstoragevpp.domain.port.out.BatteryStorageRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
