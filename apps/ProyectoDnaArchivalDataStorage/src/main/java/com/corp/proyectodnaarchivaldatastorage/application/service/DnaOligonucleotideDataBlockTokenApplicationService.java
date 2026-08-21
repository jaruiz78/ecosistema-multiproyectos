package com.corp.proyectodnaarchivaldatastorage.application.service;

import com.corp.proyectodnaarchivaldatastorage.domain.model.DnaOligonucleotideDataBlockToken;
import com.corp.proyectodnaarchivaldatastorage.domain.port.in.ManageDnaOligonucleotideDataBlockTokenUseCase;
import com.corp.proyectodnaarchivaldatastorage.domain.port.out.DnaOligonucleotideDataBlockTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DnaOligonucleotideDataBlockToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DnaOligonucleotideDataBlockTokenApplicationService implements ManageDnaOligonucleotideDataBlockTokenUseCase {

    private final DnaOligonucleotideDataBlockTokenRepositoryPort repositoryPort;

    public DnaOligonucleotideDataBlockTokenApplicationService(DnaOligonucleotideDataBlockTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DnaOligonucleotideDataBlockToken createDnaOligonucleotideDataBlockToken(String tenantId, String title, double value) {
        DnaOligonucleotideDataBlockToken entity = new DnaOligonucleotideDataBlockToken(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<DnaOligonucleotideDataBlockToken> findDnaOligonucleotideDataBlockTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DnaOligonucleotideDataBlockToken processOptimization(String id, String tenantId) {
        DnaOligonucleotideDataBlockToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DnaOligonucleotideDataBlockToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
