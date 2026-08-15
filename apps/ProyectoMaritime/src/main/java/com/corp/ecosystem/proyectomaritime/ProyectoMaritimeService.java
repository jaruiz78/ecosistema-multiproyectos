package com.corp.ecosystem.proyectomaritime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoMaritimeService {
    private final ProyectoMaritimeRepository repository;
    public ProyectoMaritimeService(ProyectoMaritimeRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoMaritimeEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoMaritimeEntity entity = new ProyectoMaritimeEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoMaritimeEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
