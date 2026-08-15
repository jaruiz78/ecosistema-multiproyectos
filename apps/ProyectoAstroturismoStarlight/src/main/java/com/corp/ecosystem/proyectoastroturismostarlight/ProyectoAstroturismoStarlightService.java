package com.corp.ecosystem.proyectoastroturismostarlight;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoAstroturismoStarlightService {
    private final ProyectoAstroturismoStarlightRepository repository;
    public ProyectoAstroturismoStarlightService(ProyectoAstroturismoStarlightRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoAstroturismoStarlightEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoAstroturismoStarlightEntity entity = new ProyectoAstroturismoStarlightEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoAstroturismoStarlightEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
