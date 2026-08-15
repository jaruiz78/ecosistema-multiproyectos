package com.corp.ecosystem.proyectovpp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoVPPService {
    private final ProyectoVPPRepository repository;
    public ProyectoVPPService(ProyectoVPPRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoVPPEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoVPPEntity entity = new ProyectoVPPEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoVPPEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
