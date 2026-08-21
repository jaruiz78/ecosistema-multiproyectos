package com.corp.proyectodecentralizedclinicalbiologistics.application.service;

import com.corp.proyectodecentralizedclinicalbiologistics.domain.model.ClinicalTrialBioSampleToken;
import com.corp.proyectodecentralizedclinicalbiologistics.domain.port.in.ManageClinicalTrialBioSampleTokenUseCase;
import com.corp.proyectodecentralizedclinicalbiologistics.domain.port.out.ClinicalTrialBioSampleTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ClinicalTrialBioSampleToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ClinicalTrialBioSampleTokenApplicationService implements ManageClinicalTrialBioSampleTokenUseCase {

    private final ClinicalTrialBioSampleTokenRepositoryPort repositoryPort;

    public ClinicalTrialBioSampleTokenApplicationService(ClinicalTrialBioSampleTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ClinicalTrialBioSampleToken createClinicalTrialBioSampleToken(String tenantId, String title, double value) {
        ClinicalTrialBioSampleToken entity = new ClinicalTrialBioSampleToken(
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
    public Optional<ClinicalTrialBioSampleToken> findClinicalTrialBioSampleTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ClinicalTrialBioSampleToken processOptimization(String id, String tenantId) {
        ClinicalTrialBioSampleToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ClinicalTrialBioSampleToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
