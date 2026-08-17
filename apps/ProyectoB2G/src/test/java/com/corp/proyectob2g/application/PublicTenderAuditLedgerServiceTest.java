package com.corp.proyectob2g.application;

import com.corp.proyectob2g.domain.model.PublicProcurementContract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link PublicTenderAuditLedgerService}.
 */
class PublicTenderAuditLedgerServiceTest {

    private final PublicTenderAuditLedgerService service = new PublicTenderAuditLedgerService();

    @Test
    @DisplayName("Debe sellar ofertas públicas encadenando hashes SHA-256 de forma inmutable")
    void shouldSealTendersWithChainedHashes() {
        PublicProcurementContract contract1 = new PublicProcurementContract(
                "TENDER_2026_01", "AYUNTAMIENTO_MADRID", 50000.0, "SUBMITTED", Instant.now()
        );
        PublicProcurementContract contract2 = new PublicProcurementContract(
                "TENDER_2026_02", "DIPUTACION_VALENCIA", 75000.0, "SUBMITTED", Instant.now()
        );

        PublicTenderAuditLedgerService.SealedTenderRecord record1 = service.sealTenderBid(contract1, "{\"proposal\":\"Obra civil A\"}");
        PublicTenderAuditLedgerService.SealedTenderRecord record2 = service.sealTenderBid(contract2, "{\"proposal\":\"Suministro B\"}");

        assertNotNull(record1);
        assertNotNull(record2);
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000", record1.previousSealHash());
        assertEquals(record1.currentSealHash(), record2.previousSealHash());

        Optional<PublicTenderAuditLedgerService.SealedTenderRecord> found = service.getSealedTender("TENDER_2026_01");
        assertTrue(found.isPresent());
        assertEquals(record1.currentSealHash(), found.get().currentSealHash());
    }
}
