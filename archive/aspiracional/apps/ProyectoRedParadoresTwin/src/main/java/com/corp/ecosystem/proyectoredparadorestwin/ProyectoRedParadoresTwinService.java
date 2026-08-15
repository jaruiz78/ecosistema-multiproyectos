package com.corp.ecosystem.proyectoredparadorestwin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoRedParadoresTwinService {
    private final ProyectoRedParadoresTwinRepository repository;
    public ProyectoRedParadoresTwinService(ProyectoRedParadoresTwinRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoRedParadoresTwinEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoRedParadoresTwinEntity entity = new ProyectoRedParadoresTwinEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoRedParadoresTwinEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
