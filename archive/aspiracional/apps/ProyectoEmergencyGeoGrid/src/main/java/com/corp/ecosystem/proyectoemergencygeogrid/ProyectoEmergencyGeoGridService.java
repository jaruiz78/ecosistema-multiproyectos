package com.corp.ecosystem.proyectoemergencygeogrid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoEmergencyGeoGridService {
    private final ProyectoEmergencyGeoGridRepository repository;
    public ProyectoEmergencyGeoGridService(ProyectoEmergencyGeoGridRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoEmergencyGeoGridEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoEmergencyGeoGridEntity entity = new ProyectoEmergencyGeoGridEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoEmergencyGeoGridEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
