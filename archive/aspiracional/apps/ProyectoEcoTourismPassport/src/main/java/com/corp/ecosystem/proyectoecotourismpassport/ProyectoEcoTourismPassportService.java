package com.corp.ecosystem.proyectoecotourismpassport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoEcoTourismPassportService {
    private final ProyectoEcoTourismPassportRepository repository;
    public ProyectoEcoTourismPassportService(ProyectoEcoTourismPassportRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoEcoTourismPassportEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoEcoTourismPassportEntity entity = new ProyectoEcoTourismPassportEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoEcoTourismPassportEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
