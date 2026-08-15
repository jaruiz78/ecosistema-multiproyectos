package com.corp.ecosystem.proyectoporttwinautonomous;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoPortTwinAutonomousService {
    private final ProyectoPortTwinAutonomousRepository repository;
    public ProyectoPortTwinAutonomousService(ProyectoPortTwinAutonomousRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoPortTwinAutonomousEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoPortTwinAutonomousEntity entity = new ProyectoPortTwinAutonomousEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoPortTwinAutonomousEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
