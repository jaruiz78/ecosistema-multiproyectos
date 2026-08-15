package com.corp.ecosystem.proyectocatastrofes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoCatastrofesService {
    private final ProyectoCatastrofesRepository repository;
    public ProyectoCatastrofesService(ProyectoCatastrofesRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoCatastrofesEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoCatastrofesEntity entity = new ProyectoCatastrofesEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoCatastrofesEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
