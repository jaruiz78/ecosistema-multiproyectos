package com.corp.ecosystem.minerals;

import com.corp.ecosystem.minerals.application.CriticalMineralsMrvService;
import com.corp.ecosystem.minerals.domain.BatteryMineralPassport;
import com.corp.ecosystem.minerals.domain.port.BatteryPassportRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoCriticalMineralsMRV.
 */
class BatteryMineralPassportTest {

    static class InMemoryBatteryPassportRepository implements BatteryPassportRepositoryPort {
        private final Map<BatteryMineralPassport.PassportId, BatteryMineralPassport> storage = new ConcurrentHashMap<>();

        @Override
        public BatteryMineralPassport save(BatteryMineralPassport passport) {
            storage.put(passport.id(), passport);
            return passport;
        }

        @Override
        public Optional<BatteryMineralPassport> findById(BatteryMineralPassport.PassportId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryBatteryPassportRepository repository = new InMemoryBatteryPassportRepository();
    private final CriticalMineralsMrvService service = new CriticalMineralsMrvService(repository);

    @Test
    @DisplayName("Debe certificar pasaporte de batería conforme con las cuotas EU CRMA 2026")
    void shouldCertifyCompliantBatteryPassport() {
        BatteryMineralPassport passport = service.certifyBattery(
                "oem-volkswagen-group",
                "BAT-MEB-82KWH-2026-001",
                8.5,  // 8.5 kg Li
                12.0, // 12 kg Co
                45.0, // 45 kg Ni
                8.0,  // 8% Recycled Li (> 6%)
                18.5, // 18.5% Recycled Co (> 16%)
                9.0,  // 9% Recycled Ni (> 6%)
                55.0  // 55 kg CO2/kWh
        );

        assertNotNull(passport.id());
        assertEquals(BatteryMineralPassport.PassportStatus.EU_CRMA_COMPLIANT, passport.status());
        assertTrue(passport.proofSeal().isVerified());
    }

    @Test
    @DisplayName("Debe marcar como NO_COMPLIANT si la cuota de cobalto reciclado no alcanza el 16%")
    void shouldDetectNonComplianceWhenRecycledCobaltTooLow() {
        BatteryMineralPassport passport = service.certifyBattery(
                "oem-generic-auto",
                "BAT-LOW-RECYCLE-002",
                10.0,
                15.0,
                50.0,
                5.0,  // 5% (< 6%)
                10.0, // 10% (< 16%)
                5.0,  // 5% (< 6%)
                85.0
        );

        assertEquals(BatteryMineralPassport.PassportStatus.NON_COMPLIANT_TARGETS_MISSED, passport.status());
    }
}
