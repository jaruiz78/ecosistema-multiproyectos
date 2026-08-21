package com.corp.proyectoplasmonicsurfacebiosensors.application.service;

import com.corp.proyectoplasmonicsurfacebiosensors.domain.model.PlasmonicResonanceShiftToken;
import com.corp.proyectoplasmonicsurfacebiosensors.domain.port.in.ManagePlasmonicResonanceShiftTokenUseCase;
import com.corp.proyectoplasmonicsurfacebiosensors.domain.port.out.PlasmonicResonanceShiftTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PlasmonicResonanceShiftToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlasmonicResonanceShiftTokenApplicationService implements ManagePlasmonicResonanceShiftTokenUseCase {

    private final PlasmonicResonanceShiftTokenRepositoryPort repositoryPort;

    public PlasmonicResonanceShiftTokenApplicationService(PlasmonicResonanceShiftTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlasmonicResonanceShiftToken createPlasmonicResonanceShiftToken(String tenantId, String title, double value) {
        PlasmonicResonanceShiftToken entity = new PlasmonicResonanceShiftToken(
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
    public Optional<PlasmonicResonanceShiftToken> findPlasmonicResonanceShiftTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlasmonicResonanceShiftToken processOptimization(String id, String tenantId) {
        PlasmonicResonanceShiftToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlasmonicResonanceShiftToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
