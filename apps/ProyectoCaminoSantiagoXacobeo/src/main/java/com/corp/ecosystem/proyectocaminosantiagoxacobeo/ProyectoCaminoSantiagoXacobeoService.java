package com.corp.ecosystem.proyectocaminosantiagoxacobeo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCaminoSantiagoXacobeoService {
    private final ProyectoCaminoSantiagoXacobeoRepository repository;
    public ProyectoCaminoSantiagoXacobeoService(ProyectoCaminoSantiagoXacobeoRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCaminoSantiagoXacobeoEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCaminoSantiagoXacobeoEntity entity = new ProyectoCaminoSantiagoXacobeoEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCaminoSantiagoXacobeoEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
