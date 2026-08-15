package com.corp.ecosystem.proyectosyntheticbiologyfoundry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoSyntheticBiologyFoundryService {
    private final ProyectoSyntheticBiologyFoundryRepository repository;
    public ProyectoSyntheticBiologyFoundryService(ProyectoSyntheticBiologyFoundryRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoSyntheticBiologyFoundryEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoSyntheticBiologyFoundryEntity entity = new ProyectoSyntheticBiologyFoundryEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoSyntheticBiologyFoundryEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
