package com.corp.ecosystem.xacobeo.domain.port;

import com.corp.ecosystem.xacobeo.domain.PilgrimDigitalCredential;
import java.util.Optional;

public interface PilgrimCredentialRepositoryPort {
    PilgrimDigitalCredential save(PilgrimDigitalCredential credential);
    Optional<PilgrimDigitalCredential> findById(PilgrimDigitalCredential.CredentialId id);
}
