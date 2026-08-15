package com.corp.ecosystem.proyectoseamlessintermodalhub;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSeamlessIntermodalHubService {
    private final ProyectoSeamlessIntermodalHubRepository repository;
    public ProyectoSeamlessIntermodalHubService(ProyectoSeamlessIntermodalHubRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSeamlessIntermodalHubEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSeamlessIntermodalHubEntity entity = new ProyectoSeamlessIntermodalHubEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSeamlessIntermodalHubEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
