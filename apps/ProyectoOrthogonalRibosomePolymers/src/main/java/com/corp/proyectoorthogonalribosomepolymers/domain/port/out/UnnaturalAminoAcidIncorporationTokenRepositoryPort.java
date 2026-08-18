package com.corp.proyectoorthogonalribosomepolymers.domain.port.out;

import com.corp.proyectoorthogonalribosomepolymers.domain.model.UnnaturalAminoAcidIncorporationToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface UnnaturalAminoAcidIncorporationTokenRepositoryPort {
    UnnaturalAminoAcidIncorporationToken save(UnnaturalAminoAcidIncorporationToken entity);
    Optional<UnnaturalAminoAcidIncorporationToken> findById(String id, String tenantId);
}
