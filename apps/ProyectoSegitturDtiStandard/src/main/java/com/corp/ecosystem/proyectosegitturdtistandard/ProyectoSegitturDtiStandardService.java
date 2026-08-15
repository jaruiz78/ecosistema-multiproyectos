package com.corp.ecosystem.proyectosegitturdtistandard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSegitturDtiStandardService {
    private final ProyectoSegitturDtiStandardRepository repository;
    public ProyectoSegitturDtiStandardService(ProyectoSegitturDtiStandardRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSegitturDtiStandardEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSegitturDtiStandardEntity entity = new ProyectoSegitturDtiStandardEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSegitturDtiStandardEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
