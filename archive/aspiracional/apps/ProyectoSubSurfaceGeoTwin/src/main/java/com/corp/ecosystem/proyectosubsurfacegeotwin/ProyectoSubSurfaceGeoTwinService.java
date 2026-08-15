package com.corp.ecosystem.proyectosubsurfacegeotwin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSubSurfaceGeoTwinService {
    private final ProyectoSubSurfaceGeoTwinRepository repository;
    public ProyectoSubSurfaceGeoTwinService(ProyectoSubSurfaceGeoTwinRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSubSurfaceGeoTwinEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSubSurfaceGeoTwinEntity entity = new ProyectoSubSurfaceGeoTwinEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSubSurfaceGeoTwinEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
