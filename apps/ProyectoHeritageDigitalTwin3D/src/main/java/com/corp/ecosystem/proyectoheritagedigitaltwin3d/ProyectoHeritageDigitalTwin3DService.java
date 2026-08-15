package com.corp.ecosystem.proyectoheritagedigitaltwin3d;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoHeritageDigitalTwin3DService {
    private final ProyectoHeritageDigitalTwin3DRepository repository;
    public ProyectoHeritageDigitalTwin3DService(ProyectoHeritageDigitalTwin3DRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoHeritageDigitalTwin3DEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoHeritageDigitalTwin3DEntity entity = new ProyectoHeritageDigitalTwin3DEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoHeritageDigitalTwin3DEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
