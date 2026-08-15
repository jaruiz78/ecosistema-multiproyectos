package com.corp.ecosystem.cruise;

import com.corp.ecosystem.cruise.application.CruiseMrvService;
import com.corp.ecosystem.cruise.domain.CruiseVoyageMrv;
import com.corp.ecosystem.cruise.domain.port.CruiseVoyageRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class CruiseVoyageMrvTest {

    static class InMemoryCruiseVoyageRepository implements CruiseVoyageRepositoryPort {
        private final Map<CruiseVoyageMrv.VoyageId, CruiseVoyageMrv> storage = new ConcurrentHashMap<>();

        @Override
        public CruiseVoyageMrv save(CruiseVoyageMrv voyage) {
            storage.put(voyage.id(), voyage);
            return voyage;
        }

        @Override
        public Optional<CruiseVoyageMrv> findById(CruiseVoyageMrv.VoyageId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryCruiseVoyageRepository repository = new InMemoryCruiseVoyageRepository();
    private final CruiseMrvService service = new CruiseMrvService(repository);

    @Test
    @DisplayName("Debe certificar viaje de crucero sostenible conforme a FuelEU Maritime 2026")
    void shouldCertifyCompliantCruiseVoyage() {
        CruiseVoyageMrv voyage = service.certifyCruiseVoyage(
                "royal-caribbean-group",
                "IMO9829922",
                "Icon of the Seas II",
                250.0, // GNL
                180.0, // Metanol Verde
                15.0,
                82.5,  // < 89.34 gCO2/MJ
                "ESBCN", // Puerto de Barcelona
                true   // Conexión eléctrica a tierra (Cold Ironing)
        );

        assertNotNull(voyage.id());
        assertEquals(CruiseVoyageMrv.ComplianceStatus.FUELEU_MARITIME_COMPLIANT, voyage.status());
        assertTrue(voyage.portCall().isColdIroningOnshorePowerUsed());
    }
}
