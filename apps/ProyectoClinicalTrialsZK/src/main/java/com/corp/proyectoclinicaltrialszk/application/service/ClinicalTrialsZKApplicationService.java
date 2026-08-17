package com.corp.proyectoclinicaltrialszk.application.service;

import com.corp.proyectoclinicaltrialszk.domain.model.ClinicalTrialsZK;
import com.corp.proyectoclinicaltrialszk.domain.port.in.ManageClinicalTrialsZKUseCase;
import com.corp.proyectoclinicaltrialszk.domain.port.out.ClinicalTrialsZKRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class ClinicalTrialsZKApplicationService implements ManageClinicalTrialsZKUseCase {

    private final ClinicalTrialsZKRepositoryPort repositoryPort;

    public ClinicalTrialsZKApplicationService(ClinicalTrialsZKRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ClinicalTrialsZK createClinicalTrialsZK(String tenantId, String title, double value) {
        ClinicalTrialsZK entity = new ClinicalTrialsZK(
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
    public Optional<ClinicalTrialsZK> findClinicalTrialsZKById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ClinicalTrialsZK processOptimization(String id, String tenantId) {
        ClinicalTrialsZK existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ClinicalTrialsZK optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
