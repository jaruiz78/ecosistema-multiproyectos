package com.corp.ecosystem.proyectohoteltwinrevpar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoHotelTwinRevPARService {
    private final ProyectoHotelTwinRevPARRepository repository;
    public ProyectoHotelTwinRevPARService(ProyectoHotelTwinRevPARRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoHotelTwinRevPAREntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoHotelTwinRevPAREntity entity = new ProyectoHotelTwinRevPAREntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoHotelTwinRevPAREntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
