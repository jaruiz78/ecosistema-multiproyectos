package com.corp.proyectostratosphericaerosolgeoengineering.domain.model;

import java.io.Serializable;

/**
 * Pluma de inyección estratosférica de aerosoles de sulfato / calcita (SAI)
 * para forzamiento radiativo negativo y enfriamiento albedo global.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record StratosphericAerosolPlume(
        String injectionId,
        double altitudeKm,
        double injectedMassMegatonnesSo2,
        double aerosolOpticalDepthAod,
        double radiativeForcingWattsPerM2,
        PlumeDispersionStatus status
) implements Serializable {

    public enum PlumeDispersionStatus {
        ZONAL_DISPERSION,
        GLOBAL_MERIDIONAL_DIFFUSION,
        TROPOSPHERIC_WASHOUT
    }

    public static StratosphericAerosolPlume create(String id, double altitudeKm, double massMt) {
        // Modelo simplificado de forzamiento radiativo: \Delta F \approx -2.5 \cdot \text{AOD}
        double aod = massMt * 0.08;
        double rf = -2.5 * aod;
        return new StratosphericAerosolPlume(id, altitudeKm, massMt, aod, rf, PlumeDispersionStatus.ZONAL_DISPERSION);
    }
}
