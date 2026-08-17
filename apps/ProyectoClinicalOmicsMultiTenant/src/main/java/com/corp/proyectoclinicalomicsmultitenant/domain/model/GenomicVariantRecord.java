package com.corp.proyectoclinicalomicsmultitenant.domain.model;

import java.io.Serializable;

/**
 * Registro de variante genómica clínica anonimizada (Zero-PII) con frecuencia alélica y patogenicidad.
 */
public record GenomicVariantRecord(
        String variantId,
        String tenantHospitalId,
        String chromosome,
        long positionBp,
        String referenceAllele,
        String alternateAllele,
        double alleleFrequency,
        ClinicalSignificance significance
) implements Serializable {

    public enum ClinicalSignificance {
        PATHOGENIC,
        LIKELY_PATHOGENIC,
        UNCERTAIN_SIGNIFICANCE,
        BENIGN
    }

    public static GenomicVariantRecord create(String variantId, String hospitalId, String chr, long pos, String ref, String alt) {
        return new GenomicVariantRecord(variantId, hospitalId, chr, pos, ref, alt, 0.0012, ClinicalSignificance.UNCERTAIN_SIGNIFICANCE);
    }

    public GenomicVariantRecord reclassify(ClinicalSignificance newSig, double updatedFreq) {
        return new GenomicVariantRecord(variantId, tenantHospitalId, chromosome, positionBp, referenceAllele, alternateAllele, updatedFreq, newSig);
    }
}
