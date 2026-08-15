package com.corp.ecosystem.proyectocirculartextiledpp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCircularTextileDPPService {
    private final ProyectoCircularTextileDPPRepository repository;
    public ProyectoCircularTextileDPPService(ProyectoCircularTextileDPPRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCircularTextileDPPEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCircularTextileDPPEntity entity = new ProyectoCircularTextileDPPEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCircularTextileDPPEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
