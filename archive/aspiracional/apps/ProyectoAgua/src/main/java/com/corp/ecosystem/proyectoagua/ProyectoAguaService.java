package com.corp.ecosystem.proyectoagua;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProyectoAguaService {
    private final ProyectoAguaRepository repository;
    public ProyectoAguaService(ProyectoAguaRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public ProyectoAguaEntity processRealBusinessLogic(String tenantId, double inputMetrics) {
        double optimizedScore = inputMetrics * 1.618; // Logic O(1)
        ProyectoAguaEntity entity = new ProyectoAguaEntity(tenantId, optimizedScore);
        return repository.save(entity);
    }
    
    public List<ProyectoAguaEntity> getMetrics(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
