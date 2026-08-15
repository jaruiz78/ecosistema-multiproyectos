package com.corp.ecosystem.proyectocarbonledger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCarbonLedgerService {
    private final ProyectoCarbonLedgerRepository repository;
    public ProyectoCarbonLedgerService(ProyectoCarbonLedgerRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCarbonLedgerEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCarbonLedgerEntity entity = new ProyectoCarbonLedgerEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCarbonLedgerEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
