package com.corp.ecosystem.proyectomiceconferencetwin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoMiceConferenceTwinService {
    private final ProyectoMiceConferenceTwinRepository repository;
    public ProyectoMiceConferenceTwinService(ProyectoMiceConferenceTwinRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoMiceConferenceTwinEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoMiceConferenceTwinEntity entity = new ProyectoMiceConferenceTwinEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoMiceConferenceTwinEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
