package com.corp.ecosystem.proyectodiputacionturismorural;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoDiputacionTurismoRuralService {
    private final ProyectoDiputacionTurismoRuralRepository repository;
    public ProyectoDiputacionTurismoRuralService(ProyectoDiputacionTurismoRuralRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoDiputacionTurismoRuralEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoDiputacionTurismoRuralEntity entity = new ProyectoDiputacionTurismoRuralEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoDiputacionTurismoRuralEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
