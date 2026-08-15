package com.corp.ecosystem.proyectodroneairspaceuspace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoDroneAirspaceUSpaceService {
    private final ProyectoDroneAirspaceUSpaceRepository repository;
    public ProyectoDroneAirspaceUSpaceService(ProyectoDroneAirspaceUSpaceRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoDroneAirspaceUSpaceEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoDroneAirspaceUSpaceEntity entity = new ProyectoDroneAirspaceUSpaceEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoDroneAirspaceUSpaceEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
