package com.corp.ecosystem.proyectofleetcoldchain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoFleetColdChainService {
    private final ProyectoFleetColdChainRepository repository;
    public ProyectoFleetColdChainService(ProyectoFleetColdChainRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoFleetColdChainEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoFleetColdChainEntity entity = new ProyectoFleetColdChainEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoFleetColdChainEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
