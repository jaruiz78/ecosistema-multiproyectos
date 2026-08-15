package com.corp.ecosystem.proyectob2g;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoB2GService {
    private final ProyectoB2GRepository repository;
    public ProyectoB2GService(ProyectoB2GRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoB2GEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoB2GEntity entity = new ProyectoB2GEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoB2GEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
