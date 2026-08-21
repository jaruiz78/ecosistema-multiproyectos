package com.corp.proyectoquantumentropyrngnetwork.application.service;

import com.corp.proyectoquantumentropyrngnetwork.domain.model.QrngEntropySourceBlockToken;
import com.corp.proyectoquantumentropyrngnetwork.domain.port.in.ManageQrngEntropySourceBlockTokenUseCase;
import com.corp.proyectoquantumentropyrngnetwork.domain.port.out.QrngEntropySourceBlockTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QrngEntropySourceBlockToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QrngEntropySourceBlockTokenApplicationService implements ManageQrngEntropySourceBlockTokenUseCase {

    private final QrngEntropySourceBlockTokenRepositoryPort repositoryPort;

    public QrngEntropySourceBlockTokenApplicationService(QrngEntropySourceBlockTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QrngEntropySourceBlockToken createQrngEntropySourceBlockToken(String tenantId, String title, double value) {
        QrngEntropySourceBlockToken entity = new QrngEntropySourceBlockToken(
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
    public Optional<QrngEntropySourceBlockToken> findQrngEntropySourceBlockTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QrngEntropySourceBlockToken processOptimization(String id, String tenantId) {
        QrngEntropySourceBlockToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QrngEntropySourceBlockToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
