package com.corp.ecosystem.proyectoregenerativeexperience;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoRegenerativeExperienceService {
    private final ProyectoRegenerativeExperienceRepository repository;
    public ProyectoRegenerativeExperienceService(ProyectoRegenerativeExperienceRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoRegenerativeExperienceEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoRegenerativeExperienceEntity entity = new ProyectoRegenerativeExperienceEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoRegenerativeExperienceEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
