package com.corp.ecosystem.proyectoglobalcruisemrv;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoGlobalCruiseMRVService {
    private final ProyectoGlobalCruiseMRVRepository repository;
    public ProyectoGlobalCruiseMRVService(ProyectoGlobalCruiseMRVRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoGlobalCruiseMRVEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoGlobalCruiseMRVEntity entity = new ProyectoGlobalCruiseMRVEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoGlobalCruiseMRVEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
