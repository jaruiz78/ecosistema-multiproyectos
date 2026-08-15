package com.corp.ecosystem.proyectoquantumresistantrwa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoQuantumResistantRWAService {
    private final ProyectoQuantumResistantRWARepository repository;
    public ProyectoQuantumResistantRWAService(ProyectoQuantumResistantRWARepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoQuantumResistantRWAEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoQuantumResistantRWAEntity entity = new ProyectoQuantumResistantRWAEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoQuantumResistantRWAEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
