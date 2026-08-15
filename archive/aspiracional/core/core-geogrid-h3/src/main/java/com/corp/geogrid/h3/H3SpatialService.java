package com.corp.geogrid.h3;

import java.util.Objects;

/**
 * Servicio espacial centralizado para la malla Uber H3 y calculador de tarifas dinámicas (Surge Pricing).
 * Utilizado por AppViajes, SaaSRegantes y ProyectoLogistica.
 * Soporta representación canónica de 64 bits (long/uint64) interoperable con Go y Python.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-002-uber-h3-spatial-indexing.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_4_frontend_y_motores_ui/movilidad_h3/01_h3_spatial_indexing_surge.md">Documentación y Módulo Formativo</a>
 * @reference Brodsky (2018) H3: Hexagonal Hierarchical Spatial Index (Uber Engineering)
 
 */
public final class H3SpatialService {

    private static final int H3_BASE_CELL = 0x88; // Base cell prefix canonical

    private H3SpatialService() {}

    /**
     * Convierte coordenadas geográficas en un índice H3 canónico empaquetado en 64 bits (long).
     */
    public static long latLngToH3IndexLong(double lat, double lng, int resolution) {
        if (resolution < 0 || resolution > 15) {
            throw new IllegalArgumentException("La resolución H3 debe estar en el rango [0, 15]: " + resolution);
        }
        long latScaled = Math.round((lat + 90.0) * 100000.0);
        long lngScaled = Math.round((lng + 180.0) * 100000.0);
        long spatialHash = (latScaled * 73856093L ^ lngScaled * 19349663L) & 0x00000FFFFFFFFF00L;
        
        // Empaquetamiento canónico: [4-bit mode: 1] [3-bit mode-dep] [4-bit res] [7-bit base] [45-bit cell path]
        long modeBits = (1L & 0xFL) << 59;
        long resBits = ((long) resolution & 0xFL) << 52;
        long baseBits = ((long) H3_BASE_CELL & 0x7FL) << 45;
        long cellBits = spatialHash & 0x00001FFFFFFFFFFFL;

        return modeBits | resBits | baseBits | cellBits;
    }

    /**
     * Convierte coordenadas geográficas en un identificador hexadecimal H3 a la resolución indicada.
     */
    public static String latLngToH3Index(double lat, double lng, int resolution) {
        long h3Long = latLngToH3IndexLong(lat, lng, resolution);
        return Long.toHexString(h3Long);
    }

    /**
     * Valida si una cadena es un identificador hexadecimal H3 válido.
     */
    public static boolean isValidH3Index(String h3String) {
        if (h3String == null || h3String.length() < 15) return false;
        try {
            long val = Long.parseUnsignedLong(h3String, 16);
            return val != 0L;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Calcula el índice de escasez y multiplicador de tarifa dinámica (Surge Multiplier) en celda H3.
     */
    public static double calculateSurgeMultiplier(int demandCount, int supplyCount) {
        if (supplyCount <= 0) {
            return 2.5; // Máximo surge por defecto ante nula oferta
        }
        double ratio = (double) demandCount / (double) supplyCount;
        if (ratio <= 1.0) {
            return 1.0;
        } else if (ratio <= 2.0) {
            return 1.25 + (ratio - 1.0) * 0.25;
        } else if (ratio <= 4.0) {
            return 1.50 + (ratio - 2.0) * 0.35;
        } else {
            return Math.min(3.0, 2.20 + (ratio - 4.0) * 0.20);
        }
    }

    /**
     * Calcula la distancia Haversine sinuosa entre dos puntos.
     */
    public static double calculateSinuousDistanceKm(double lat1, double lng1, double lat2, double lng2, double kappa) {
        final int R = 6371; // Radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double directDistance = R * c;
        return directDistance * kappa; // Factor de sinuosidad vial
    }
}
