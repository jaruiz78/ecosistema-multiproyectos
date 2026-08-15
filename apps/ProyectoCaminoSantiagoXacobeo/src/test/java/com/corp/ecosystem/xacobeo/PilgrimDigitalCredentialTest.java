package com.corp.ecosystem.xacobeo;

import com.corp.ecosystem.xacobeo.application.CaminoSantiagoService;
import com.corp.ecosystem.xacobeo.domain.PilgrimDigitalCredential;
import com.corp.ecosystem.xacobeo.domain.port.PilgrimCredentialRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class PilgrimDigitalCredentialTest {

    static class InMemoryPilgrimRepository implements PilgrimCredentialRepositoryPort {
        private final Map<PilgrimDigitalCredential.CredentialId, PilgrimDigitalCredential> storage = new ConcurrentHashMap<>();

        @Override
        public PilgrimDigitalCredential save(PilgrimDigitalCredential credential) {
            storage.put(credential.id(), credential);
            return credential;
        }

        @Override
        public Optional<PilgrimDigitalCredential> findById(PilgrimDigitalCredential.CredentialId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryPilgrimRepository repository = new InMemoryPilgrimRepository();
    private final CaminoSantiagoService service = new CaminoSantiagoService(repository);

    @Test
    @DisplayName("Debe otorgar elegibilidad de Compostela al superar 100 km sellados en la credencial digital")
    void shouldGrantCompostelaAfter100Km() {
        PilgrimDigitalCredential cred = service.issueCredential("xunta-de-galicia-xacobeo", "0x98127398127", "Camino Francés");

        service.recordStageStamp(cred.id(), "Sarria", 0x88390cb307fffffL, 114.0, "ZK-SARRIA-PROOF");
        PilgrimDigitalCredential updated = service.recordStageStamp(cred.id(), "Santiago de Compostela", 0x88390cb307fffffL, 114.0, "ZK-CATEDRAL-PROOF");

        assertEquals(PilgrimDigitalCredential.CompostelaStatus.ELIGIBLE_100KM_COMPLETED, updated.compostelaStatus());
        assertEquals(2, updated.stamps().size());
    }
}
