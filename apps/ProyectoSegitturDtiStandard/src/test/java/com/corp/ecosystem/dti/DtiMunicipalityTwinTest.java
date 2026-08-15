package com.corp.ecosystem.dti;

import com.corp.ecosystem.dti.application.SegitturDtiService;
import com.corp.ecosystem.dti.domain.DtiMunicipalityTwin;
import com.corp.ecosystem.dti.domain.port.DtiMunicipalityRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class DtiMunicipalityTwinTest {

    static class InMemoryDtiRepository implements DtiMunicipalityRepositoryPort {
        private final Map<DtiMunicipalityTwin.MunicipalityId, DtiMunicipalityTwin> storage = new ConcurrentHashMap<>();

        @Override
        public DtiMunicipalityTwin save(DtiMunicipalityTwin twin) {
            storage.put(twin.id(), twin);
            return twin;
        }

        @Override
        public Optional<DtiMunicipalityTwin> findById(DtiMunicipalityTwin.MunicipalityId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryDtiRepository repository = new InMemoryDtiRepository();
    private final SegitturDtiService service = new SegitturDtiService(repository);

    @Test
    @DisplayName("Debe auditar municipio y otorgar Distintivo DTI_CERTIFIED_EXCELLENCE bajo UNE 178501")
    void shouldCertifyDtiExcellence() {
        DtiMunicipalityTwin twin = service.auditMunicipality(
                "benidorm-smartcity",
                "Benidorm",
                "Comunitat Valenciana",
                88.0, // Gobernanza
                85.0, // Sostenibilidad
                82.0, // Accesibilidad (>75%)
                90.0, // Innovación
                94.0  // Tecnología
        );

        assertNotNull(twin.id());
        assertEquals(DtiMunicipalityTwin.SegitturDistinctionLevel.DTI_CERTIFIED_EXCELLENCE, twin.distinctionLevel());
        assertTrue(twin.axesScores().overallAveragePct() >= 80.0);
    }
}
