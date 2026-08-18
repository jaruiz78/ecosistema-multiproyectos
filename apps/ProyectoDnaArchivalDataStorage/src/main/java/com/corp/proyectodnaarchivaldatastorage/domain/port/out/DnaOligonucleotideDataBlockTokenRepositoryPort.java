package com.corp.proyectodnaarchivaldatastorage.domain.port.out;

import com.corp.proyectodnaarchivaldatastorage.domain.model.DnaOligonucleotideDataBlockToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DnaOligonucleotideDataBlockTokenRepositoryPort {
    DnaOligonucleotideDataBlockToken save(DnaOligonucleotideDataBlockToken entity);
    Optional<DnaOligonucleotideDataBlockToken> findById(String id, String tenantId);
}
