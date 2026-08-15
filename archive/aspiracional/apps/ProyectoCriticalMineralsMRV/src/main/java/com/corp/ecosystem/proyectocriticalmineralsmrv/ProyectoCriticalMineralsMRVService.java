package com.corp.ecosystem.proyectocriticalmineralsmrv;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCriticalMineralsMRVService {
    private final ProyectoCriticalMineralsMRVRepository repository;
    public ProyectoCriticalMineralsMRVService(ProyectoCriticalMineralsMRVRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCriticalMineralsMRVEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCriticalMineralsMRVEntity entity = new ProyectoCriticalMineralsMRVEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCriticalMineralsMRVEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
