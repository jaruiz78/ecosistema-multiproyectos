package com.corp.ecosystem.proyectov2g;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoV2GService {
    private final ProyectoV2GRepository repository;
    public ProyectoV2GService(ProyectoV2GRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoV2GEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoV2GEntity entity = new ProyectoV2GEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoV2GEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
