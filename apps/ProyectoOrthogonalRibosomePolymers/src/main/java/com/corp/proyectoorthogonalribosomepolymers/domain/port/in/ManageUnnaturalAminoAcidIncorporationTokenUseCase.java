package com.corp.proyectoorthogonalribosomepolymers.domain.port.in;

import com.corp.proyectoorthogonalribosomepolymers.domain.model.UnnaturalAminoAcidIncorporationToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageUnnaturalAminoAcidIncorporationTokenUseCase {
    UnnaturalAminoAcidIncorporationToken createUnnaturalAminoAcidIncorporationToken(String tenantId, String title, double value);
    Optional<UnnaturalAminoAcidIncorporationToken> findUnnaturalAminoAcidIncorporationTokenById(String id, String tenantId);
    UnnaturalAminoAcidIncorporationToken processOptimization(String id, String tenantId);
}
