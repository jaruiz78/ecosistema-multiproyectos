package com.corp.ecosystem.xacobeo.application;

import com.corp.ecosystem.xacobeo.domain.PilgrimDigitalCredential;
import com.corp.ecosystem.xacobeo.domain.port.PilgrimCredentialRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CaminoSantiagoService {

    private final PilgrimCredentialRepositoryPort repositoryPort;

    public CaminoSantiagoService(PilgrimCredentialRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public PilgrimDigitalCredential issueCredential(String tenantId, String pilgrimHash, String routeName) {
        PilgrimDigitalCredential.CredentialId id = new PilgrimDigitalCredential.CredentialId("XAC-" + System.nanoTime());
        PilgrimDigitalCredential cred = PilgrimDigitalCredential.issue(id, tenantId, pilgrimHash, routeName);
        return repositoryPort.save(cred);
    }

    public PilgrimDigitalCredential recordStageStamp(
            PilgrimDigitalCredential.CredentialId id,
            String locality,
            long h3Index,
            double km,
            String zkProof
    ) {
        PilgrimDigitalCredential cred = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Credencial no encontrada: " + id.value()));

        PilgrimDigitalCredential.StageStamp stamp = new PilgrimDigitalCredential.StageStamp(
                locality, h3Index, km, System.currentTimeMillis(), zkProof
        );
        PilgrimDigitalCredential updated = cred.addStamp(stamp);
        return repositoryPort.save(updated);
    }

    public Optional<PilgrimDigitalCredential> getCredential(PilgrimDigitalCredential.CredentialId id) {
        return repositoryPort.findById(id);
    }
}
