package com.corp.proyectobiodiversitygenomicbank.domain.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Muestra metagenómica de ADN ambiental (eDNA) con cálculo del índice de biodiversidad de Shannon-Wiener.
 */
public record EnvironmentalDnaSample(
        String sampleId,
        String biomeLocation,
        long h3IndexLocation,
        Map<String, Integer> speciesReadCounts,
        double shannonDiversityIndexH,
        ConservationStatus status
) implements Serializable {

    public enum ConservationStatus {
        HIGH_DIVERSITY_PROTECTED,
        MODERATE_DIVERSITY,
        DEGRADED_HABITAT
    }

    public static EnvironmentalDnaSample create(String sampleId, String biome, long h3, Map<String, Integer> reads) {
        double totalReads = reads.values().stream().mapToInt(Integer::intValue).sum();
        double h = 0.0;
        if (totalReads > 0) {
            for (int count : reads.values()) {
                if (count > 0) {
                    double p = count / totalReads;
                    h -= p * Math.log(p);
                }
            }
        }
        ConservationStatus st = h > 2.5 ? ConservationStatus.HIGH_DIVERSITY_PROTECTED : ConservationStatus.MODERATE_DIVERSITY;
        return new EnvironmentalDnaSample(sampleId, biome, h3, reads, h, st);
    }
}
