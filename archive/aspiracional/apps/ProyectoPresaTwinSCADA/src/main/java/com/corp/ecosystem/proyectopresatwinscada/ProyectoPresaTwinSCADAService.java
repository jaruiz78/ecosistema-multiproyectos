package com.corp.ecosystem.proyectopresatwinscada;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoPresaTwinSCADAService {
    private final ProyectoPresaTwinSCADARepository repository;
    public ProyectoPresaTwinSCADAService(ProyectoPresaTwinSCADARepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoPresaTwinSCADAEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoPresaTwinSCADAEntity entity = new ProyectoPresaTwinSCADAEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoPresaTwinSCADAEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
