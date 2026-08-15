package com.corp.ecosystem.proyectoclinicaltrialszk;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoClinicalTrialsZKService {
    private final ProyectoClinicalTrialsZKRepository repository;
    public ProyectoClinicalTrialsZKService(ProyectoClinicalTrialsZKRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoClinicalTrialsZKEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoClinicalTrialsZKEntity entity = new ProyectoClinicalTrialsZKEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoClinicalTrialsZKEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
