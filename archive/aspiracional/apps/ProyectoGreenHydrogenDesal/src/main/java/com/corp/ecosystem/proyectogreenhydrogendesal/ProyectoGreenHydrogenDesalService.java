package com.corp.ecosystem.proyectogreenhydrogendesal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoGreenHydrogenDesalService {
    private final ProyectoGreenHydrogenDesalRepository repository;
    public ProyectoGreenHydrogenDesalService(ProyectoGreenHydrogenDesalRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoGreenHydrogenDesalEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoGreenHydrogenDesalEntity entity = new ProyectoGreenHydrogenDesalEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoGreenHydrogenDesalEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
