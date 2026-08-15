package com.corp.ecosystem.proyectogovprocurematch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoGovProcureMatchService {
    private final ProyectoGovProcureMatchRepository repository;
    public ProyectoGovProcureMatchService(ProyectoGovProcureMatchRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoGovProcureMatchEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoGovProcureMatchEntity entity = new ProyectoGovProcureMatchEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoGovProcureMatchEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
