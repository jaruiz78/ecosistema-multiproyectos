package com.corp.ecosystem.proyectoenergia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoEnergiaService {
    private final ProyectoEnergiaRepository repository;
    public ProyectoEnergiaService(ProyectoEnergiaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoEnergiaEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoEnergiaEntity entity = new ProyectoEnergiaEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoEnergiaEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
