package com.corp.ecosystem.proyectotokenrwa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoTokenRWAService {
    private final ProyectoTokenRWARepository repository;
    public ProyectoTokenRWAService(ProyectoTokenRWARepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoTokenRWAEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoTokenRWAEntity entity = new ProyectoTokenRWAEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoTokenRWAEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
