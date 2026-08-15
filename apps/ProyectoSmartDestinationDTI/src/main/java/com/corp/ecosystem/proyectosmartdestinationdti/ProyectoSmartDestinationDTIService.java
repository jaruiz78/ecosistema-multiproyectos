package com.corp.ecosystem.proyectosmartdestinationdti;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSmartDestinationDTIService {
    private final ProyectoSmartDestinationDTIRepository repository;
    public ProyectoSmartDestinationDTIService(ProyectoSmartDestinationDTIRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSmartDestinationDTIEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSmartDestinationDTIEntity entity = new ProyectoSmartDestinationDTIEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSmartDestinationDTIEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
