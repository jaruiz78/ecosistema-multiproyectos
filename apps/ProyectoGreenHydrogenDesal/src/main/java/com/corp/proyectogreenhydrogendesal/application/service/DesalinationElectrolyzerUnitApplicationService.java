package com.corp.proyectogreenhydrogendesal.application.service;

import com.corp.proyectogreenhydrogendesal.domain.model.DesalinationElectrolyzerUnit;
import com.corp.proyectogreenhydrogendesal.domain.port.in.ManageDesalinationElectrolyzerUnitUseCase;
import com.corp.proyectogreenhydrogendesal.domain.port.out.DesalinationElectrolyzerUnitRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DesalinationElectrolyzerUnit.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DesalinationElectrolyzerUnitApplicationService implements ManageDesalinationElectrolyzerUnitUseCase {

    private final DesalinationElectrolyzerUnitRepositoryPort repositoryPort;

    public DesalinationElectrolyzerUnitApplicationService(DesalinationElectrolyzerUnitRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DesalinationElectrolyzerUnit createDesalinationElectrolyzerUnit(String tenantId, String title, double value) {
        DesalinationElectrolyzerUnit entity = new DesalinationElectrolyzerUnit(
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
    public Optional<DesalinationElectrolyzerUnit> findDesalinationElectrolyzerUnitById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DesalinationElectrolyzerUnit processOptimization(String id, String tenantId) {
        DesalinationElectrolyzerUnit existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DesalinationElectrolyzerUnit optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
