package com.corp.proyectobiodiversitygenomicbank.application;

import com.corp.proyectobiodiversitygenomicbank.application.service.ShannonBiodiversityIndexService;
import com.corp.proyectobiodiversitygenomicbank.infrastructure.adapter.out.persistence.InMemoryEdnaSampleRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShannonBiodiversityIndexServiceTest {

    @Test
    @DisplayName("Debe procesar lecturas metagenómicas y persistir muestra eDNA")
    void testProcessMetagenomicReads() {
        var repo = new InMemoryEdnaSampleRepositoryAdapter();
        var service = new ShannonBiodiversityIndexService(repo);

        var sample = service.processMetagenomicReads(
                "SAMPLE-01",
                "DOÑANA_WETLANDS",
                0x881f1d4887fffffL,
                Map.of("Phoenicopterus_roseus", 100, "Anas_platyrhynchos", 80)
        );

        assertNotNull(sample);
        assertEquals("SAMPLE-01", sample.sampleId());
        assertTrue(sample.shannonDiversityIndexH() > 0.5);
    }
}
