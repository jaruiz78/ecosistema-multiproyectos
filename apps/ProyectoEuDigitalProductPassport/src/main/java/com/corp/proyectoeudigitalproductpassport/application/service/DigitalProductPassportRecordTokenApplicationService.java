package com.corp.proyectoeudigitalproductpassport.application.service;

import com.corp.proyectoeudigitalproductpassport.domain.model.DigitalProductPassportRecordToken;
import com.corp.proyectoeudigitalproductpassport.domain.port.in.ManageDigitalProductPassportRecordTokenUseCase;
import com.corp.proyectoeudigitalproductpassport.domain.port.out.DigitalProductPassportRecordTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DigitalProductPassportRecordToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DigitalProductPassportRecordTokenApplicationService implements ManageDigitalProductPassportRecordTokenUseCase {

    private final DigitalProductPassportRecordTokenRepositoryPort repositoryPort;

    public DigitalProductPassportRecordTokenApplicationService(DigitalProductPassportRecordTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DigitalProductPassportRecordToken createDigitalProductPassportRecordToken(String tenantId, String title, double value) {
        DigitalProductPassportRecordToken entity = new DigitalProductPassportRecordToken(
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
    public Optional<DigitalProductPassportRecordToken> findDigitalProductPassportRecordTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DigitalProductPassportRecordToken processOptimization(String id, String tenantId) {
        DigitalProductPassportRecordToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DigitalProductPassportRecordToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
