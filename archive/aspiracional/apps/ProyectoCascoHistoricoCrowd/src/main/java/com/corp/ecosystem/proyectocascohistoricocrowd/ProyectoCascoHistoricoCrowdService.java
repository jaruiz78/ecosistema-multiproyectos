package com.corp.ecosystem.proyectocascohistoricocrowd;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCascoHistoricoCrowdService {
    private final ProyectoCascoHistoricoCrowdRepository repository;
    public ProyectoCascoHistoricoCrowdService(ProyectoCascoHistoricoCrowdRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCascoHistoricoCrowdEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCascoHistoricoCrowdEntity entity = new ProyectoCascoHistoricoCrowdEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCascoHistoricoCrowdEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
