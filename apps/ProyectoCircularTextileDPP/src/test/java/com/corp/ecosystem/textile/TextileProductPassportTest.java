package com.corp.ecosystem.textile;

import com.corp.ecosystem.textile.application.CircularTextileService;
import com.corp.ecosystem.textile.domain.TextileProductPassport;
import com.corp.ecosystem.textile.domain.port.TextilePassportRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoCircularTextileDPP.
 */
class TextileProductPassportTest {

    static class InMemoryTextilePassportRepository implements TextilePassportRepositoryPort {
        private final Map<TextileProductPassport.PassportId, TextileProductPassport> storage = new ConcurrentHashMap<>();

        @Override
        public TextileProductPassport save(TextileProductPassport passport) {
            storage.put(passport.id(), passport);
            return passport;
        }

        @Override
        public Optional<TextileProductPassport> findById(TextileProductPassport.PassportId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryTextilePassportRepository repository = new InMemoryTextilePassportRepository();
    private final CircularTextileService service = new CircularTextileService(repository);

    @Test
    @DisplayName("Debe emitir pasaporte textil circular conforme a la regulación ESPR 2026")
    void shouldIssueCompliantTextilePassport() {
        TextileProductPassport passport = service.certifyGarment(
                "inditex-zara-sustainability",
                "8438201948291",
                40.0, // 40% poliéster reciclado
                35.0, // 35% algodón orgánico (Total 75% > 50%)
                5.0,
                85.0, // 85% reciclabilidad (> 70%)
                120.0, // 120 litros agua
                2.4 // 2.4 kg CO2
        );

        assertNotNull(passport.id());
        assertEquals(TextileProductPassport.PassportComplianceStatus.ESPR_COMPLIANT_CIRCULAR, passport.status());
        assertTrue(passport.proofSeal().isVerified());
    }

    @Test
    @DisplayName("Debe marcar déficit de sostenibilidad si las fibras recicladas no alcanzan el 50%")
    void shouldDetectSustainabilityDeficitWhenFibersBelow50() {
        TextileProductPassport passport = service.certifyGarment(
                "fast-fashion-brand",
                "8411122233344",
                10.0,
                15.0, // Total 25% (< 50%)
                10.0,
                40.0, // 40% (< 70%)
                950.0,
                14.5
        );

        assertEquals(TextileProductPassport.PassportComplianceStatus.NON_COMPLIANT_SUSTAINABILITY_DEFICIT, passport.status());
    }
}
