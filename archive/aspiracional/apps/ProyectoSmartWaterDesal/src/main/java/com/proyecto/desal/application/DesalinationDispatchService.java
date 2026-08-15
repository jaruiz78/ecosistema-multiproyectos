package com.proyecto.desal.application;

import com.proyecto.desal.domain.DesalinationPlant;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de despacho inteligente sincronizado con excedentes de generación solar renovable.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class DesalinationDispatchService {

    public record DesalinationSchedule(String plantId, double targetProductionRatePercent, double powerConsumedKw, double producedWaterM3) {}

    public List<DesalinationSchedule> optimizeDesalinationWithRenewables(List<DesalinationPlant> plants, double availableSurplusSolarKw) {
        if (plants == null || plants.isEmpty()) return List.of();

        double remainingSolarKw = Math.max(0.0, availableSurplusSolarKw);

        return plants.stream()
                .map(plant -> {
                    // Calcular el régimen de producción óptimo para absorber el excedente solar
                    double maxPowerKw = plant.maxProductionCapacityM3PerHour() * plant.specificEnergyKwhPerM3();
                    double targetPowerKw = Math.min(maxPowerKw, remainingSolarKw);
                    double targetRate = (maxPowerKw > 0) ? (targetPowerKw / maxPowerKw) * 100.0 : 0.0;

                    double producedM3 = (targetRate / 100.0) * plant.maxProductionCapacityM3PerHour();
                    return new DesalinationSchedule(plant.plantId(), Math.round(targetRate * 10.0) / 10.0, targetPowerKw, Math.round(producedM3 * 10.0) / 10.0);
                })
                .toList();
    }
}
