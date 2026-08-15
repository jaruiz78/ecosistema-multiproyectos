package com.corp.ecosystem.ecotasa;

import com.corp.ecosystem.ecotasa.application.EcoTaxSettlementService;
import com.corp.ecosystem.ecotasa.domain.RegionalEcoTaxSettlement;
import com.corp.ecosystem.ecotasa.domain.port.EcoTaxSettlementRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class RegionalEcoTaxSettlementTest {

    static class InMemoryEcoTaxRepository implements EcoTaxSettlementRepositoryPort {
        private final Map<RegionalEcoTaxSettlement.SettlementId, RegionalEcoTaxSettlement> storage = new ConcurrentHashMap<>();

        @Override
        public RegionalEcoTaxSettlement save(RegionalEcoTaxSettlement settlement) {
            storage.put(settlement.id(), settlement);
            return settlement;
        }

        @Override
        public Optional<RegionalEcoTaxSettlement> findById(RegionalEcoTaxSettlement.SettlementId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryEcoTaxRepository repository = new InMemoryEcoTaxRepository();
    private final EcoTaxSettlementService service = new EcoTaxSettlementService(repository);

    @Test
    @DisplayName("Debe liquidar y certificar ecotasa turística con prueba criptográfica ZK")
    void shouldSettleEcoTaxWithZkProof() {
        RegionalEcoTaxSettlement settlement = service.settleEcoTax(
                "govern-illes-balears",
                "HOTEL-PALMA-001",
                4500,
                3.50,
                "Restauración Posidonia Oceánica Parque Cabrera",
                "ZK-BALEARIC-ECOTAX-PROVED"
        );

        assertNotNull(settlement.id());
        assertEquals(15750.0, settlement.details().totalCollectedEur(), 1e-3);
        assertEquals(RegionalEcoTaxSettlement.SettlementLedgerStatus.SETTLED_AUDITED_ZK, settlement.status());
    }
}
