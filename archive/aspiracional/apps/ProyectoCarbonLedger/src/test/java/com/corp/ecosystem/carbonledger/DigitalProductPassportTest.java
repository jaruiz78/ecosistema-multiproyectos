package com.corp.ecosystem.carbonledger;

import com.corp.ecosystem.carbonledger.application.CarbonLedgerService;
import com.corp.ecosystem.carbonledger.domain.DigitalProductPassport;
import com.corp.ecosystem.carbonledger.domain.port.PassportRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoCarbonLedger.
 */
class DigitalProductPassportTest {

    static class InMemoryPassportRepository implements PassportRepositoryPort {
        private final Map<DigitalProductPassport.PassportId, DigitalProductPassport> storage = new ConcurrentHashMap<>();

        @Override
        public DigitalProductPassport save(DigitalProductPassport passport) {
            storage.put(passport.id(), passport);
            return passport;
        }

        @Override
        public Optional<DigitalProductPassport> findById(DigitalProductPassport.PassportId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryPassportRepository repository = new InMemoryPassportRepository();
    private final CarbonLedgerService service = new CarbonLedgerService(repository);

    @Test
    @DisplayName("Debe crear un pasaporte en estado DRAFT con cálculo preciso de huella neta")
    void shouldCreateDraftPassportWithNetFootprint() {
        DigitalProductPassport passport = service.createDraftPassport(
                "tenant-battery-corp",
                "BATCH-LFP-2026-001",
                DigitalProductPassport.ProductCategory.INDUSTRIAL_BATTERY,
                45.0, // raw
                30.0, // mfg
                5.0,  // log
                15.0, // avoided
                85.0, // recycled pct
                95.0, // recyclability pct
                120   // lifespan months
        );

        assertNotNull(passport.id());
        assertEquals(DigitalProductPassport.PassportState.DRAFT, passport.state());
        assertEquals(65.0, passport.footprint().totalNetKgCo2PerUnit(), 0.001);
        assertEquals(85.0, passport.circularity().recycledContentPct());
    }

    @Test
    @DisplayName("Debe certificar un pasaporte DRAFT con sello criptográfico ZK")
    void shouldCertifyDraftPassportWithZkSeal() {
        DigitalProductPassport draft = service.createDraftPassport(
                "tenant-steel-corp",
                "BATCH-GREEN-STEEL-99",
                DigitalProductPassport.ProductCategory.CONSTRUCTION_STEEL,
                100.0, 50.0, 10.0, 40.0, 90.0, 99.0, 600
        );

        DigitalProductPassport certified = service.certifyPassport(
                draft.id(),
                "0xmerkle_root_abcdef123456",
                "0xsnark_proof_987654321",
                "TUV_RHEINLAND_EU_DPP_AUTH"
        );

        assertEquals(DigitalProductPassport.PassportState.CERTIFIED, certified.state());
        assertNotNull(certified.proofSeal());
        assertEquals("TUV_RHEINLAND_EU_DPP_AUTH", certified.proofSeal().verifierAuthority());
        assertNotNull(certified.issuedAt());
    }
}
