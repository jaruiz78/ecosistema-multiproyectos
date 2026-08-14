package com.proyecto.v2g.application;

import com.proyecto.v2g.domain.V2GBatteryNode;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de aplicación para despacho y arbitraje V2G de flotas eléctricas.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class V2GDispatchService {

    public record DispatchResult(String vehicleId, double dischargedKwh, double remunerationUsd, double finalSoc) {}

    public List<DispatchResult> arbitrateFleetDischarge(List<V2GBatteryNode> fleet, double gridTariffUsdKwh, double peakTariffThresholdUsd) {
        if (fleet == null || fleet.isEmpty()) return List.of();

        // Solo inyectar energía a la red si la tarifa supera el umbral de pico de demanda
        if (gridTariffUsdKwh < peakTariffThresholdUsd) {
            return List.of();
        }

        return fleet.stream()
                .filter(v -> v.calculateAvailableDischargeKwh() > 1.0)
                .map(v -> {
                    double availableKwh = Math.min(v.maxDischargeKw() * 0.5, v.calculateAvailableDischargeKwh());
                    V2GBatteryNode updated = v.withDischarge(availableKwh);
                    double revenue = availableKwh * gridTariffUsdKwh * 0.85; // 85% para el conductor, 15% fee ecosistema
                    return new DispatchResult(v.vehicleId(), availableKwh, Math.round(revenue * 100.0) / 100.0, updated.currentSocPercent());
                })
                .toList();
    }
}
