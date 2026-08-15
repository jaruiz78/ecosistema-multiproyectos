package com.corp.ecosystem.proyectosmartwaterdesal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSmartWaterDesalService {
    private final ProyectoSmartWaterDesalRepository repository;
    public ProyectoSmartWaterDesalService(ProyectoSmartWaterDesalRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSmartWaterDesalEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSmartWaterDesalEntity entity = new ProyectoSmartWaterDesalEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSmartWaterDesalEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
