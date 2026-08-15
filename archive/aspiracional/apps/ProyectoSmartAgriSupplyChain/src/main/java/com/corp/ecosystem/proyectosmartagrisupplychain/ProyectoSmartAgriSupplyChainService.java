package com.corp.ecosystem.proyectosmartagrisupplychain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSmartAgriSupplyChainService {
    private final ProyectoSmartAgriSupplyChainRepository repository;
    public ProyectoSmartAgriSupplyChainService(ProyectoSmartAgriSupplyChainRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSmartAgriSupplyChainEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSmartAgriSupplyChainEntity entity = new ProyectoSmartAgriSupplyChainEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSmartAgriSupplyChainEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
