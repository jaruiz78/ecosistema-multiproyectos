package com.corp.ecosystem.proyectosoilbiocarbontwin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSoilBioCarbonTwinService {
    private final ProyectoSoilBioCarbonTwinRepository repository;
    public ProyectoSoilBioCarbonTwinService(ProyectoSoilBioCarbonTwinRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSoilBioCarbonTwinEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSoilBioCarbonTwinEntity entity = new ProyectoSoilBioCarbonTwinEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSoilBioCarbonTwinEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
