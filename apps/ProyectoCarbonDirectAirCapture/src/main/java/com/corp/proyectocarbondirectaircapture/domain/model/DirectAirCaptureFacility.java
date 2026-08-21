package com.corp.proyectocarbondirectaircapture.domain.model;

import java.io.Serializable;

/**
 * Instalación de captura directa de aire (DAC) con mineralización permanente en formaciones de basalto.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record DirectAirCaptureFacility(
        String facilityId,
        double dailyCaptureCapacityTonnesCo2,
        double cumulativeMineralizedTonnesCo2,
        double thermalEnergyInputKwhPerTonne,
        double mineralizationEfficiencyPct,
        FacilityStatus status
) implements Serializable {

    public enum FacilityStatus {
        ADSORBING,
        THERMAL_DESORPTION,
        INJECTING_BASALT,
        STANDBY
    }

    public static DirectAirCaptureFacility create(String id, double capacity) {
        return new DirectAirCaptureFacility(id, capacity, 0.0, 1200.0, 95.0, FacilityStatus.ADSORBING);
    }

    public DirectAirCaptureFacility recordMineralizationBatch(double capturedCo2Tonnes) {
        double mineralized = capturedCo2Tonnes * (mineralizationEfficiencyPct / 100.0);
        return new DirectAirCaptureFacility(facilityId, dailyCaptureCapacityTonnesCo2, cumulativeMineralizedTonnesCo2 + mineralized, thermalEnergyInputKwhPerTonne, mineralizationEfficiencyPct, FacilityStatus.INJECTING_BASALT);
    }
}
