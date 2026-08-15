package com.corp.ecosystem.proyectopharmacoldchain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoPharmaColdChainService {
    private final ProyectoPharmaColdChainRepository repository;
    public ProyectoPharmaColdChainService(ProyectoPharmaColdChainRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoPharmaColdChainEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoPharmaColdChainEntity entity = new ProyectoPharmaColdChainEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoPharmaColdChainEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
