package com.corp.ecosystem.proyectogeneralista;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoGeneralistaService {
    private final ProyectoGeneralistaRepository repository;
    public ProyectoGeneralistaService(ProyectoGeneralistaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoGeneralistaEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoGeneralistaEntity entity = new ProyectoGeneralistaEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoGeneralistaEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
