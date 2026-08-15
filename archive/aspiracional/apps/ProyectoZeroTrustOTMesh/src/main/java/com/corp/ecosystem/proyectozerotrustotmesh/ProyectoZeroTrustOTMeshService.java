package com.corp.ecosystem.proyectozerotrustotmesh;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoZeroTrustOTMeshService {
    private final ProyectoZeroTrustOTMeshRepository repository;
    public ProyectoZeroTrustOTMeshService(ProyectoZeroTrustOTMeshRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoZeroTrustOTMeshEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoZeroTrustOTMeshEntity entity = new ProyectoZeroTrustOTMeshEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoZeroTrustOTMeshEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
