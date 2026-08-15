package com.corp.ecosystem.proyectosmartstreetlightingv2g;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSmartStreetLightingV2GService {
    private final ProyectoSmartStreetLightingV2GRepository repository;
    public ProyectoSmartStreetLightingV2GService(ProyectoSmartStreetLightingV2GRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSmartStreetLightingV2GEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSmartStreetLightingV2GEntity entity = new ProyectoSmartStreetLightingV2GEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSmartStreetLightingV2GEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
