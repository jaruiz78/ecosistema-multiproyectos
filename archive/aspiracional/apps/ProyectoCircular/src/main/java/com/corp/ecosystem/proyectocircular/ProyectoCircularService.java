package com.corp.ecosystem.proyectocircular;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCircularService {
    private final ProyectoCircularRepository repository;
    public ProyectoCircularService(ProyectoCircularRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCircularEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCircularEntity entity = new ProyectoCircularEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCircularEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
