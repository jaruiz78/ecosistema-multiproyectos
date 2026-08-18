package com.corp.proyectodnaarchivaldatastorage.domain.port.in;

import com.corp.proyectodnaarchivaldatastorage.domain.model.DnaOligonucleotideDataBlockToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDnaOligonucleotideDataBlockTokenUseCase {
    DnaOligonucleotideDataBlockToken createDnaOligonucleotideDataBlockToken(String tenantId, String title, double value);
    Optional<DnaOligonucleotideDataBlockToken> findDnaOligonucleotideDataBlockTokenById(String id, String tenantId);
    DnaOligonucleotideDataBlockToken processOptimization(String id, String tenantId);
}
