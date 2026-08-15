package com.corp.ecosystem.proyectoairlineinterlinebaggage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoAirlineInterlineBaggageService {
    private final ProyectoAirlineInterlineBaggageRepository repository;
    public ProyectoAirlineInterlineBaggageService(ProyectoAirlineInterlineBaggageRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoAirlineInterlineBaggageEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoAirlineInterlineBaggageEntity entity = new ProyectoAirlineInterlineBaggageEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoAirlineInterlineBaggageEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
