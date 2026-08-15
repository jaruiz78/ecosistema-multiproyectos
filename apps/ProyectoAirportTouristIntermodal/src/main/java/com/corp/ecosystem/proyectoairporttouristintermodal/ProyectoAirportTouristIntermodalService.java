package com.corp.ecosystem.proyectoairporttouristintermodal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoAirportTouristIntermodalService {
    private final ProyectoAirportTouristIntermodalRepository repository;
    public ProyectoAirportTouristIntermodalService(ProyectoAirportTouristIntermodalRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoAirportTouristIntermodalEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoAirportTouristIntermodalEntity entity = new ProyectoAirportTouristIntermodalEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoAirportTouristIntermodalEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
