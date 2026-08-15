package com.corp.ecosystem.soilbiocarbon;

import com.corp.ecosystem.soilbiocarbon.application.SoilBioCarbonService;
import com.corp.ecosystem.soilbiocarbon.domain.SoilCarbonParcelTwin;
import com.corp.ecosystem.soilbiocarbon.domain.port.SoilParcelRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoSoilBioCarbonTwin.
 */
class SoilCarbonParcelTwinTest {

    static class InMemorySoilParcelRepository implements SoilParcelRepositoryPort {
        private final Map<SoilCarbonParcelTwin.ParcelId, SoilCarbonParcelTwin> storage = new ConcurrentHashMap<>();

        @Override
        public SoilCarbonParcelTwin save(SoilCarbonParcelTwin parcel) {
            storage.put(parcel.id(), parcel);
            return parcel;
        }

        @Override
        public Optional<SoilCarbonParcelTwin> findById(SoilCarbonParcelTwin.ParcelId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemorySoilParcelRepository repository = new InMemorySoilParcelRepository();
    private final SoilBioCarbonService service = new SoilBioCarbonService(repository);

    @Test
    @DisplayName("Debe evaluar parcela agrícola y certificar elegibilidad Verra VM0042 con aumento de SOC y microbioma sano")
    void shouldEvaluateAndCertifyEligibleSoilCarbonParcel() {
        SoilCarbonParcelTwin parcel = service.certifyParcelCarbon(
                "cooperativa-valle-guadalquivir",
                0x88390cb307fffffL,
                50.0, // 50 hectáreas
                0.35, // 35% ratio micorrízico (> 25%)
                420.0, // 420 mg/kg biomasa microbiana
                1.85,
                1.20, // 1.2% SOC base
                1.85  // 1.85% SOC actual (+0.65% secuestro)
        );

        assertNotNull(parcel.id());
        assertEquals(SoilCarbonParcelTwin.CarbonCreditEligibilityStatus.VERRA_VM0042_ELIGIBLE, parcel.eligibilityStatus());
        assertTrue(parcel.balance().netSequestrationTonsCo2ePerYear() > 0);
    }

    @Test
    @DisplayName("Debe marcar déficit de suelo si el SOC no ha aumentado respecto a la línea base")
    void shouldDetectDepletedSoilWhenNoSocIncrease() {
        SoilCarbonParcelTwin parcel = service.certifyParcelCarbon(
                "finca-secano-intensivo",
                0x88390cb307fffffL,
                20.0,
                0.10, // Pobre en micorrizas
                110.0,
                0.90,
                1.40,
                1.30 // Pérdida de SOC
        );

        assertEquals(SoilCarbonParcelTwin.CarbonCreditEligibilityStatus.DEFICIT_SOIL_DEPLETED, parcel.eligibilityStatus());
        assertEquals(0.0, parcel.balance().netSequestrationTonsCo2ePerYear());
    }
}
