package com.corp.ecosystem.paradores.application;

import com.corp.ecosystem.paradores.domain.HistoricParadorTwin;
import com.corp.ecosystem.paradores.domain.port.ParadorRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class HistoricParadorService {

    private final ParadorRepositoryPort repositoryPort;

    public HistoricParadorService(ParadorRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public HistoricParadorTwin monitorHistoricParador(
            String tenantId,
            String name,
            String monumentCategory,
            double indoorTemp,
            double targetTemp,
            double geothermalKw,
            double humidityPct
    ) {
        HistoricParadorTwin.ParadorId id = new HistoricParadorTwin.ParadorId("PARADOR-" + System.nanoTime());
        HistoricParadorTwin.BuildingThermalProfile thermal = new HistoricParadorTwin.BuildingThermalProfile(
                indoorTemp, targetTemp, geothermalKw, humidityPct
        );
        HistoricParadorTwin parador = HistoricParadorTwin.evaluateParador(id, tenantId, name, monumentCategory, thermal);
        return repositoryPort.save(parador);
    }

    public Optional<HistoricParadorTwin> getParador(HistoricParadorTwin.ParadorId id) {
        return repositoryPort.findById(id);
    }
}
