package com.corp.ecosystem.proyectoturismotermalbalnearios;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoTurismoTermalBalneariosService {
    private final ProyectoTurismoTermalBalneariosRepository repository;
    public ProyectoTurismoTermalBalneariosService(ProyectoTurismoTermalBalneariosRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoTurismoTermalBalneariosEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoTurismoTermalBalneariosEntity entity = new ProyectoTurismoTermalBalneariosEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoTurismoTermalBalneariosEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
