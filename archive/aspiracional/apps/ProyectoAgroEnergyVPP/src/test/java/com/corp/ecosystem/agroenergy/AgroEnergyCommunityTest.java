package com.corp.ecosystem.agroenergy;

import com.corp.ecosystem.agroenergy.application.AgroEnergyVppService;
import com.corp.ecosystem.agroenergy.domain.AgroEnergyCommunity;
import com.corp.ecosystem.agroenergy.domain.port.AgroEnergyRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoAgroEnergyVPP.
 */
class AgroEnergyCommunityTest {

    static class InMemoryAgroEnergyRepository implements AgroEnergyRepositoryPort {
        private final Map<AgroEnergyCommunity.CommunityId, AgroEnergyCommunity> storage = new ConcurrentHashMap<>();

        @Override
        public AgroEnergyCommunity save(AgroEnergyCommunity community) {
            storage.put(community.id(), community);
            return community;
        }

        @Override
        public Optional<AgroEnergyCommunity> findById(AgroEnergyCommunity.CommunityId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryAgroEnergyRepository repository = new InMemoryAgroEnergyRepository();
    private final AgroEnergyVppService service = new AgroEnergyVppService(repository);

    @Test
    @DisplayName("Debe despachar autoconsumo solar y cargar excedente en batería cuando hay sobreproducción")
    void shouldDispatchSolarAndChargeBatteryOnSurplus() {
        AgroEnergyCommunity community = service.registerCommunity(
                "cr-genil-cabria",
                "Comunidad de Regantes Genil Cabria",
                500.0, // 500 kWp
                420.0, // Generando 420 kW
                300.0, // Batería 300 kWh
                50.0,  // SoC 50%
                100.0, // Max 100 kW charge
                List.of(
                        new AgroEnergyCommunity.PumpStationLoad("BOMBA-ESTE-1", 150.0, true, 1.0),
                        new AgroEnergyCommunity.PumpStationLoad("BOMBA-OESTE-2", 100.0, true, 1.0)
                )
        );

        // Demanda = 250 kW | Generación = 420 kW | Excedente = 170 kW
        AgroEnergyCommunity.DispatchInstruction dispatch = service.calculateDispatch(community.id(), 120.0); // 120 EUR/MWh

        assertEquals(250.0, dispatch.solarSelfConsumptionKw(), 0.001);
        assertEquals(-100.0, dispatch.batteryChargeDischargeKw(), 0.001); // Carga a tope (100 kW)
        assertEquals(70.0, dispatch.gridInjectionKw(), 0.001); // Inyección de remanente (70 kW)
        assertEquals(0.0, dispatch.gridImportKw(), 0.001);
        assertTrue(dispatch.estimatedHourlySavingsEur() > 0.0);
    }

    @Test
    @DisplayName("Debe descargar batería en déficit solar sin necesidad de importar de red si hay SoC")
    void shouldDischargeBatteryOnDeficitWithoutGridImport() {
        AgroEnergyCommunity community = service.registerCommunity(
                "cr-vega-baja",
                "Comunidad Regantes Vega Baja",
                500.0,
                50.0,  // Generando solo 50 kW (atardecer)
                400.0,
                80.0,  // SoC 80%
                150.0, // Max discharge 150 kW
                List.of(
                        new AgroEnergyCommunity.PumpStationLoad("BOMBA-1", 120.0, true, 1.0)
                )
        );

        // Demanda = 120 kW | Solar = 50 kW | Déficit = 70 kW
        AgroEnergyCommunity.DispatchInstruction dispatch = service.calculateDispatch(community.id(), 150.0);

        assertEquals(50.0, dispatch.solarSelfConsumptionKw(), 0.001);
        assertEquals(70.0, dispatch.batteryChargeDischargeKw(), 0.001); // Descarga 70 kW
        assertEquals(0.0, dispatch.gridImportKw(), 0.001); // 0 importación
    }
}
