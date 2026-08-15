package com.corp.ecosystem.coldchain.application;

import com.corp.ecosystem.coldchain.domain.ColdChainFleetPlan;
import com.corp.ecosystem.coldchain.domain.port.FleetDispatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ColdChainFleetService {

    private final FleetDispatchRepositoryPort repositoryPort;

    public ColdChainFleetService(FleetDispatchRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public ColdChainFleetPlan createPlan(
            String tenantId,
            String vehicleId,
            ColdChainFleetPlan.TemperatureCategory category,
            List<ColdChainFleetPlan.DeliveryStop> stops
    ) {
        ColdChainFleetPlan.ThermalRange range = new ColdChainFleetPlan.ThermalRange(category.getMinTemp(), category.getMaxTemp());
        ColdChainFleetPlan plan = new ColdChainFleetPlan(
                new ColdChainFleetPlan.RoutePlanId("ROUTE-" + System.nanoTime()),
                tenantId,
                vehicleId,
                category,
                range,
                stops != null ? List.copyOf(stops) : List.of(),
                List.of(),
                ColdChainFleetPlan.PlanState.IN_TRANSIT,
                Instant.now()
        );
        return repositoryPort.save(plan);
    }

    public ColdChainFleetPlan ingestThermalTelemetry(ColdChainFleetPlan.RoutePlanId id, double tempCelsius, double humidityPct) {
        ColdChainFleetPlan plan = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + id.value()));

        ColdChainFleetPlan updated = plan.recordThermalReading(tempCelsius, humidityPct, System.currentTimeMillis());
        return repositoryPort.save(updated);
    }

    public Optional<ColdChainFleetPlan> getPlan(ColdChainFleetPlan.RoutePlanId id) {
        return repositoryPort.findById(id);
    }
}
