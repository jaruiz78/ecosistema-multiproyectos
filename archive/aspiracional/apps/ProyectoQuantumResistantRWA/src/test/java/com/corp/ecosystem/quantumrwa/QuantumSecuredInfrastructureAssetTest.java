package com.corp.ecosystem.quantumrwa;

import com.corp.ecosystem.quantumrwa.application.QuantumResistantRwaService;
import com.corp.ecosystem.quantumrwa.domain.QuantumSecuredInfrastructureAsset;
import com.corp.ecosystem.quantumrwa.domain.port.QuantumAssetRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoQuantumResistantRWA.
 */
class QuantumSecuredInfrastructureAssetTest {

    static class InMemoryQuantumAssetRepository implements QuantumAssetRepositoryPort {
        private final Map<QuantumSecuredInfrastructureAsset.AssetTokenId, QuantumSecuredInfrastructureAsset> storage = new ConcurrentHashMap<>();

        @Override
        public QuantumSecuredInfrastructureAsset save(QuantumSecuredInfrastructureAsset asset) {
            storage.put(asset.id(), asset);
            return asset;
        }

        @Override
        public Optional<QuantumSecuredInfrastructureAsset> findById(QuantumSecuredInfrastructureAsset.AssetTokenId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryQuantumAssetRepository repository = new InMemoryQuantumAssetRepository();
    private final QuantumResistantRwaService service = new QuantumResistantRwaService(repository);

    @Test
    @DisplayName("Debe tokenizar infraestructura pública (Planta Desaladora) con criptografía post-cuántica ML-KEM")
    void shouldTokenizePublicAssetWithPostQuantumSignatures() {
        QuantumSecuredInfrastructureAsset asset = service.tokenizePublicAsset(
                "gobierno-canarias-infraestructuras",
                "Planta Desaladora Las Palmas III",
                BigDecimal.valueOf(80000000.00), // 80M EUR
                800000,                          // 800,000 fracciones a 100 EUR
                6.85                             // 6.85% rendimiento anual
        );

        assertNotNull(asset.id());
        assertEquals(QuantumSecuredInfrastructureAsset.TokenizationStatus.MICA_COMPLIANT_ACTIVE, asset.status());
        assertTrue(asset.pqProof().isPostQuantumVerified());
        assertNotNull(asset.pqProof().nistMlDsaSignature());
        assertEquals(BigDecimal.valueOf(100.0000).stripTrailingZeros(), asset.valuation().pricePerFractionEur().stripTrailingZeros());
    }
}
