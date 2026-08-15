package com.corp.ecosystem.proyectosalud;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSaludService {
    private final ProyectoSaludRepository repository;
    public ProyectoSaludService(ProyectoSaludRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSaludEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSaludEntity entity = new ProyectoSaludEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSaludEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
