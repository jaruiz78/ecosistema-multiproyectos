package com.corp.proyectoclinicalomicsmultitenant.domain;

import com.corp.proyectoclinicalomicsmultitenant.domain.model.GenomicVariantRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenomicVariantRecordTest {

    @Test
    @DisplayName("Debe reclasificar variante patogénica cuando la frecuencia alélica poblacional es rara")
    void testReclassifyVariant() {
        GenomicVariantRecord record = GenomicVariantRecord.create("VAR-BRCA1-01", "HOSP-LA-FE-VALENCIA", "chr17", 41276045, "C", "T");
        var reclassified = record.reclassify(GenomicVariantRecord.ClinicalSignificance.PATHOGENIC, 0.00002);

        assertEquals(GenomicVariantRecord.ClinicalSignificance.PATHOGENIC, reclassified.significance());
        assertEquals(0.00002, reclassified.alleleFrequency(), 1e-6);
    }
}
