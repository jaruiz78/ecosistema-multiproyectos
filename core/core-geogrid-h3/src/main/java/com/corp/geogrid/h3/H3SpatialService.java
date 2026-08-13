package com.corp.geogrid.h3;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio espacial centralizado para la malla Uber H3 y calculador de tarifas dinámicas (Surge Pricing).
 * Utilizado por AppViajes, SaaSRegantes y ProyectoLogistica.
 */
public final class H3SpatialService {

    private H3SpatialService() {}

    /**
     * Convierte coordenadas geográficas en un identificador de celda H3 simulado a resolución 8.
     */
    public static String latLngToH3Index(double lat, double lng, int resolution) {
        long latScaled = Math.round(lat * 10000);
        long lngScaled = Math.round(lng * 10000);
        long h3Hash = Math.abs(latScaled * 31 + lngScaled) % 0xFFFFFFFFFFL;
        return String.format("8%x1072b59ff%03x", resolution, h3Hash % 4096);
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
